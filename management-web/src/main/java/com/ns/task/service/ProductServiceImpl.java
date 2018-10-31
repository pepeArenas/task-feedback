package com.ns.task.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ns.task.config.properties.RabbitConfig;
import com.ns.task.model.ProductDTO;
import com.ns.task.services.ProductService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private static final Logger logger = LogManager.getLogger();
    private static final List<ProductDTO> products;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    private ObjectMapper mapper = new ObjectMapper();


    static {
        products = new ArrayList<>();
        products.add(new ProductDTO(1, "SCW090", "SCREWDRIVER", new BigDecimal("11.34"), true, null));
        products.add(new ProductDTO(2, "SCW091", "SCREWDRIVER", new BigDecimal("12.34"), true, null));
        products.add(new ProductDTO(3, "SCW092", "SCREWDRIVER", new BigDecimal("13.34"), true, null));
    }


    @Override
    public List<ProductDTO> getProducts() {
        try {
            String productAsJSON = mapper.writeValueAsString("getAllProducts");
            Object message = rabbitTemplate.convertSendAndReceive(RabbitConfig.EXCHANGE_GET,
                    RabbitConfig.ROUTING_KEY_GET,
                    productAsJSON);
            String messageAsString = new String((byte[]) message);
            logger.debug("Message recived from RabbitMQ {}", messageAsString);
            ProductDTO[] products = mapper.readValue(messageAsString, ProductDTO[].class);
            for (ProductDTO productConverted : products) {
                logger.debug("After convert JSON-POJO {}", productConverted);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return products;
    }

    @Override
    public ProductDTO insertProduct(ProductDTO product) {

        try {
            String productAsJSON = mapper.writeValueAsString(product);
            Object message = rabbitTemplate.convertSendAndReceive(RabbitConfig.EXCHANGE,
                    RabbitConfig.ROUTING_KEY,
                    productAsJSON);
            String messageAsString = new String((byte[]) message);
            logger.debug("Message recived from RabbitMQ {}", messageAsString);
            ProductDTO productDTO = mapper.readValue(messageAsString, ProductDTO.class);
            logger.debug("After convert JSON-POJO {}", productDTO);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return product;

    }
}
