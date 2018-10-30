package com.ns.task.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ns.task.controllers.ProductController;
import com.ns.task.model.ProductDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ProductWebMessageListener {
    private static final Logger logger = LogManager.getLogger();
    private final ProductController controller;
    private ObjectMapper mapper = new ObjectMapper();
    private static final String QUEUE = "q.management.insert.ex";

    @Autowired
    public ProductWebMessageListener(ProductController controller) {
        this.controller = controller;
    }

    @RabbitListener(queues = QUEUE)
    public void listener(Message message) {
        byte[] body = message.getBody();
        String messageBody = new String(body);
        ProductDTO product = new ProductDTO();
        logger.debug("Reading data to RabbitMQ {}", messageBody);
        try {

            product = mapper.readValue(messageBody, ProductDTO.class);
            logger.debug("Sending data converted from message to save ProductDTO {}", product);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
