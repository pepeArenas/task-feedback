package com.ns.task.service;

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
    private static final Logger logger = LogManager.getLogger();

    public ProductCoreServiceImpl() {
    }

    @Autowired
    public ProductCoreServiceImpl(ModelMapper mapper, ProductRepository repository) {
        this.mapper = mapper;
        this.repository = repository;
    }

    @RabbitListener(queues = "q.management.get")
    public List<ProductDTO> receiverForAllProductsRPC(ProductDTO product) {
        List<ProductDTO> products = getProducts();
        logger.debug("Products returned form DB {}", products);
        return products;
    }


    @Override
    public List<ProductDTO> getProducts() {
        List<ProductEntity> products = repository.retrieveProducts();
        logger.debug("Number of returned products from DB {}", products.size());
        return products.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @RabbitListener(queues = "q.management.insert")
    public ProductDTO reciverRPC(ProductDTO product) {
        logger.info("Received message from RabbitMQ: {}", product.toString());
        ProductDTO productSaved = insertProduct(product);
        return productSaved;
    }

    @Override
    public ProductDTO insertProduct(ProductDTO product) {
        ProductEntity productEntity = convertToEntity(product);
        try {
            logger.debug("Sending data to DB {}", productEntity);
            productEntity = repository.saveProduct(productEntity);
            product.setComplete(false);
            product.setMessage("Name and model of the service exists already");
            logger.debug("Getting persisted data from insert to DB {}", productEntity);
        } catch (DataIntegrityViolationException exception) {
            product.setComplete(false);
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
