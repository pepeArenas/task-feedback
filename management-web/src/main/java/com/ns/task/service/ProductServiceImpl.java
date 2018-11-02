package com.ns.task.service;

import com.ns.task.config.properties.RabbitConfig;
import com.ns.task.model.ProductDTO;
import com.ns.task.services.ProductService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private static final Logger logger = LogManager.getLogger();
    private static final String GET_ALL_PRODUCTS = "getAllProducts";
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public ProductServiceImpl(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public List<ProductDTO> getProducts() {
        return (List<ProductDTO>) rabbitTemplate.convertSendAndReceive(RabbitConfig.EXCHANGE_GET,
                RabbitConfig.ROUTING_KEY_GET,
                GET_ALL_PRODUCTS);
    }

    @Override
    public ProductDTO insertProduct(ProductDTO product) {
        ProductDTO productDTO = (ProductDTO) rabbitTemplate.convertSendAndReceive(RabbitConfig.EXCHANGE,
                RabbitConfig.ROUTING_KEY,
                product);

        logger.debug(productDTO);

        return productDTO;

    }
}
