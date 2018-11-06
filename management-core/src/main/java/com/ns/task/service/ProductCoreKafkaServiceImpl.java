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
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Profile("kafka")
public class ProductCoreKafkaServiceImpl implements ProductService {
    private static final Logger logger = LogManager.getLogger();
    private ModelMapper mapper;
    private ProductRepository repository;

    @Autowired
    public void setMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    @Autowired
    public void setRepository(ProductRepository repository) {
        this.repository = repository;
    }

    @KafkaListener(topics = "t.get", containerFactory = "kafkaListenerContainerFactory")
    public List<ProductDTO> receiverForAllProductsRPC(ProductDTO message) {
        final List<ProductDTO> products = getProducts();
        logger.debug("Products returned form DB {}", products);
        return products;
    }


    @Override
    public List<ProductDTO> getProducts() {
        final List<ProductEntity> products = repository.retrieveProducts();
        logger.debug("Number of returned products from DB {}", products.size());
        return products.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @KafkaListener(topics = "t.insert", containerFactory = "kafkaListenerContainerFactory")
    public ProductDTO receiverRPC(ProductDTO product) {
        logger.info("Received message from Kafka: {}", product.toString());
        return insertProduct(product);
    }

    @Override
    public ProductDTO insertProduct(ProductDTO product) {
        ProductEntity productEntity = convertToEntity(product);
        try {
            logger.debug("Sending data to DB {}", productEntity);
            productEntity = repository.saveProduct(productEntity);
            product.setMessage("Name and model of the service exists already");
            logger.debug("Getting persisted data from insert to DB {}", productEntity);
        } catch (DataIntegrityViolationException exception) {
            product.setMessage("Name and model already exists");
            return product;
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
