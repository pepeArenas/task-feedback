package com.ns.task.service;

import com.ns.task.config.properties.CommonProperties;
import com.ns.task.config.properties.RabbitConfig;
import com.ns.task.entities.ProductEntity;
import com.ns.task.model.ProductDTO;
import com.ns.task.repositories.ProductRepository;
import com.ns.task.services.ProductService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductCoreServiceImpl implements ProductService {
    private ModelMapper mapper;
    private ProductRepository repository;
    private static final Logger LOGGER = LogManager.getLogger();

    @Autowired
    public ProductCoreServiceImpl(
            ModelMapper mapper,
            ProductRepository repository) {
        this.mapper = mapper;
        this.repository = repository;
    }

    @RabbitListener(queues = RabbitConfig.QUEUE_GET)
    public List<ProductDTO> receiverForAllProductsRPC(String message) {
        final List<ProductDTO> products = getProducts();
        LOGGER.debug("Products returned form DB {}", products);
        return products;
    }


    @Override
    public List<ProductDTO> getProducts() {
        final List<ProductEntity> products = repository.retrieveProducts();
        LOGGER.debug("Number of returned products from DB {}", products.size());
        return products.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @RabbitListener(queues = RabbitConfig.QUEUE)
    public ProductDTO receiverRPC(ProductDTO product) {
        LOGGER.info("Received message from RabbitMQ: {}", product.toString());
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
            productEntity.setMessage(CommonProperties.DUPLICATE_PRODUCT);
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
