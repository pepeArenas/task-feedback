package com.ns.task.service;

import com.ns.task.entities.ProductEntity;
import com.ns.task.model.ProductDTO;
import com.ns.task.repositories.ProductRepository;
import com.ns.task.services.ProductService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
@Profile("kafka")
public class ProductCoreKafkaServiceImpl implements ProductService {
    private static final Logger LOGGER = LogManager.getLogger();
    private ModelMapper mapper;
    private ProductRepository repository;
    private KafkaTemplate<String, ProductDTO[]> kafkaTemplate;
    private static final String RESPONSE_TOPIC = "t.resultado";

    @Autowired
    public void setKafkaTemplate(KafkaTemplate<String, ProductDTO[]> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Autowired
    public void setMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    @Autowired
    public void setRepository(ProductRepository repository) {
        this.repository = repository;
    }

    @KafkaListener(topics = "t.get", containerFactory = "kafkaProductListenerContainerFactory")
    public ProductDTO[] receiverForAllProductsRPC(ProductDTO[] message) {
        final List<ProductDTO> products = getProducts();
        LOGGER.debug("Products returned form DB {}", products);
        ProductDTO[] productsAsArray = new ProductDTO[products.size()];
        for (int i = 0; i < products.size(); i++) {
            productsAsArray[i] = products.get(i);
        }
        final ListenableFuture<SendResult<String, ProductDTO[]>> future = kafkaTemplate.send(RESPONSE_TOPIC, productsAsArray);
        try {
            future.get();
        } catch (InterruptedException e) {
            LOGGER.error("An exception has occurred while thread was sleep {}", e);
        } catch (ExecutionException e) {
            LOGGER.error("An exception has occurred while asynchronous task block the thread {}", e);
        }
        future.addCallback(new ListenableFutureCallback<SendResult<String, ProductDTO[]>>() {
            @Override
            public void onFailure(Throwable throwable) {
                LOGGER.error("An error has occurred when trying send a message {}", throwable);
            }

            @Override
            public void onSuccess(SendResult<String, ProductDTO[]> stringSendResult) {
                LOGGER.debug("The message has been sent successfully" +
                                "Partition: {}, " +
                                "Topic: {}, " +
                                "Offset: {}", stringSendResult.getRecordMetadata().partition(),
                        stringSendResult.getRecordMetadata().topic(),
                        stringSendResult.getRecordMetadata().offset());
            }
        });
        return productsAsArray;
    }


    @Override
    public List<ProductDTO> getProducts() {
        final List<ProductEntity> products = repository.retrieveProducts();
        LOGGER.debug("Number of returned products from DB {}", products.size());
        return products.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @KafkaListener(topics = "t.insert", containerFactory = "kafkaListenerContainerFactory")
    public ProductDTO receiverRPC(ProductDTO product) {
        LOGGER.debug("Received message from Kafka: {}", product.toString());
        return insertProduct(product);
    }

    @Override
    public ProductDTO insertProduct(ProductDTO product) {
        ProductEntity productEntity = convertToEntity(product);
        try {
            LOGGER.debug("Sending data to DB {}", productEntity);
            productEntity = repository.saveProduct(productEntity);
            LOGGER.debug("Getting persisted data from insert to DB {}", productEntity);
        } catch (DataIntegrityViolationException exception) {
            productEntity.setMessage("Name and model already exists");
        }
        return convertToDto(productEntity);
    }

    private ProductDTO convertToDto(ProductEntity entity) {
        return mapper.map(entity, ProductDTO.class);
    }

    private ProductEntity convertToEntity(ProductDTO dto) {
        return mapper.map(dto, ProductEntity.class);
    }
}
