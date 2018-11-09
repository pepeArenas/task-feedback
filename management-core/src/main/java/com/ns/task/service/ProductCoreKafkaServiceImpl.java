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
import java.util.stream.Collectors;

@Service
@Profile("kafka")
public class ProductCoreKafkaServiceImpl implements ProductService {
    private static final Logger LOGGER = LogManager.getLogger();
    private ModelMapper mapper;
    private ProductRepository repository;
    private KafkaTemplate<String, ProductDTO[]> kafkaTemplate;
    private KafkaTemplate<String, ProductDTO> productTemplate;
    private static final String RESPONSE_PRODUCTS_TOPIC = "t.products";
    private static final String RESPONSE_PRODUCT_TOPIC = "t.product";

    @Autowired
    public void setProductTemplate(KafkaTemplate<String, ProductDTO> productTemplate) {
        this.productTemplate = productTemplate;
    }

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


    private ProductDTO[] convertListToArray(List<ProductDTO> list) {
        return list.stream().toArray(ProductDTO[]::new);
    }

    @KafkaListener(topics = "t.get", containerFactory = "kafkaProductListenerContainerFactory")
    public ProductDTO[] receiverForAllProductsRPC(ProductDTO[] message) {
        final List<ProductDTO> products = getProducts();
        LOGGER.debug("Products returned form DB {}", products);
        ProductDTO[] productsAsArray = convertListToArray(products);
        final ListenableFuture<SendResult<String, ProductDTO[]>> future = kafkaTemplate.send(RESPONSE_PRODUCTS_TOPIC, productsAsArray);
        messageSentSuccessfully(future);
        return productsAsArray;
    }

    private void messageSentSuccessfully(ListenableFuture<SendResult<String, ProductDTO[]>> future) {
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
        ProductDTO productPersisted = insertProduct(product);
        productTemplate.send(RESPONSE_PRODUCT_TOPIC, productPersisted);
        return productPersisted;
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
