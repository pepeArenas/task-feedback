package com.ns.task.listeners;

import com.ns.task.model.ProductDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Service
@Profile("kafka")
public class TopicListener {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final String RESPONSE_PRODUCT_TOPIC = "t.product";
    public static Map<String, ProductDTO> productsReturned = new ConcurrentHashMap<>();

    @KafkaListener(topics = RESPONSE_PRODUCT_TOPIC, containerFactory = "kafkaListenerContainerFactory")
    public ProductDTO readProductsFromTopic(ProductDTO product) {
        LOGGER.debug("The product returned from Kakfa is: {}", product);
        productsReturned.put(product.getUUID(), product);
        return product;
    }
}
