package com.ns.task.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ns.task.model.ProductDTO;
import com.ns.task.services.ProductService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ProductMessageListener {
    private static final Logger logger = LogManager.getLogger();
    private final ProductService productService;
    private ObjectMapper mapper = new ObjectMapper();
    private static final String QUEUE = "q.management.insert";

    @Autowired
    public ProductMessageListener(ProductService productService) {
        this.productService = productService;
    }

    @RabbitListener(queues = QUEUE, containerFactory = "jsaFactory")
    public void listener(Message message) {
        byte[] body = message.getBody();
        String messageBody = new String(body);
        logger.debug("Reading data to RabbitMQ {}", messageBody);
        try {

            ProductDTO product = mapper.readValue(messageBody, ProductDTO.class);
            logger.debug("Sending data converted from message to save ProductDTO {}", product);
            productService.insertProduct(product);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
