package com.ns.task.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ns.task.config.ProducerCore;
import com.ns.task.entities.ProductEntity;
import com.ns.task.model.ProductDTO;
import com.ns.task.repositories.ProductRepository;
import com.ns.task.services.ProductService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductCoreServiceImpl implements ProductService {


    private ModelMapper mapper;
    private ProductRepository repository;
    private static final Logger logger = LogManager.getLogger();
    private AmqpTemplate amqpTemplate;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    ProducerCore producerCore;

    public ProductCoreServiceImpl() {
    }

    @Autowired
    public ProductCoreServiceImpl(ModelMapper mapper, ProductRepository repository) {
        this.mapper = mapper;
        this.repository = repository;
    }

    @RabbitListener(queues = "q.management.get", containerFactory = "jsaFactory")
    public List<ProductDTO> reciverForAllProductsRPC(Message message) {
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

    @RabbitListener(queues = "q.management.insert", containerFactory = "jsaFactory")
    public String reciverRPC(Message message) {
        byte[] body = message.getBody();
        String messageBody = new String(body);
        ProductDTO product = new ProductDTO();
        String JSON = "";
        logger.debug("Reading data to RabbitMQ {}", messageBody);
        try {
            product = objectMapper.readValue(messageBody, ProductDTO.class);
            logger.debug("Sending data converted from message to save ProductDTO {}", product);
            product = insertProduct(product);
            JSON = objectMapper.writeValueAsString(product);
            logger.debug("JSON {}", JSON);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return JSON;
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
            //throw new ProductManagementException("Name and model of the service exists already", exception);
            product.setComplete(false);
            product.setMessage("Name and model of the service exists already");
            try {
                producerCore.produce(product);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
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
