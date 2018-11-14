package com.ns.task.controllers;

import com.ns.task.listeners.TopicListener;
import com.ns.task.model.ProductDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@Profile("kafka")
public class TopicProductsController {
    private static final Logger LOGGER = LogManager.getLogger();

    @PostMapping("topics")
    public ProductDTO getProductsFromTopics(@RequestParam("uuid") String uuid) {
        LOGGER.debug("Product to find {}", uuid);
        final Map<String, ProductDTO> productsReturned = TopicListener.productsReturned;
        LOGGER.debug("Products returned from kafka listener {}", TopicListener.productsReturned);
        final ProductDTO productDTO = productsReturned.get(uuid);
        LOGGER.debug("Product find {}", productDTO);
        return productDTO;
    }

}
