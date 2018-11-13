package com.ns.task.controllers;

import com.ns.task.listeners.TopicListener;
import com.ns.task.model.ProductDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@Profile("kafka")
public class TopicProductsController {
    private static final Logger LOGGER = LogManager.getLogger();

    @PostMapping("topics")
    @ResponseBody
    public String getProductsFromTopics(@RequestParam("uuid") String uuid, ModelMap model) {
        final Map<String, ProductDTO> productsReturned = TopicListener.productsReturned;
        String result = "";
        final ProductDTO productDTO = productsReturned.get(uuid);
        if (productDTO == null) {
            LOGGER.debug("The product with UUID {} is not in the pending list {}", uuid, productsReturned);
        } else {
            LOGGER.debug("The product with UUID {} is in the pending list {}", productDTO.getUUID(), productsReturned);
            model.put("name", productDTO.getName());
            model.put("model", productDTO.getModel());
            model.put("showWaitingDiv", "display: none");
            if (productDTO.getMessage() == null) {
                result = "<h1>Product Added</h1></b><h3>Product:" + productDTO.getName() + " Model: " + productDTO.getModel() + " added successfully</h3>";
            } else {
                result = "<h1>An Exception has occurred</h1></b><h3>" + productDTO.getMessage() + "</h3>";
            }
            productsReturned.remove(productDTO.getUUID());

        }

        return result;
    }

}
