package com.ns.task.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ns.task.config.properties.RabbitConfig;
import com.ns.task.model.ProductDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class Producer {
    private AmqpTemplate amqpTemplate;

    @Autowired
    public Producer(AmqpTemplate amqpTemplate) {
        this.amqpTemplate = amqpTemplate;
    }

    private static final Logger logger = LogManager.getLogger();
    private ObjectMapper mapper = new ObjectMapper();

    public void produce(ProductDTO product) throws JsonProcessingException {
        String productAsJSON = mapper.writeValueAsString(product);
        Object o = amqpTemplate.convertSendAndReceive(RabbitConfig.EXCHANGE, RabbitConfig.ROUTING_KEY, productAsJSON);
        logger.info("Sending message {}", o);
        logger.info("Sending message = {} to queue {}", productAsJSON, RabbitConfig.QUEUE);
    }
}