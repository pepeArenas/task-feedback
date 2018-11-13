package com.ns.task.service;

import com.ns.task.config.properties.rabbitMQ.RabbitMQProperties;
import com.ns.task.model.ProductDTO;
import com.ns.task.services.ProductService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Profile("rabbitMQ")
public class ProductServiceImpl implements ProductService {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final String GET_ALL_PRODUCTS = "getAllProducts";
    private final RabbitTemplate rabbitTemplate;
    private final RabbitMQProperties properties;

    @Autowired
    public ProductServiceImpl(RabbitTemplate rabbitTemplate, RabbitMQProperties properties) {
        this.rabbitTemplate = rabbitTemplate;
        this.properties = properties;
    }

    @Override
    public List<ProductDTO> getProducts() {
        return (List<ProductDTO>) rabbitTemplate.convertSendAndReceive(properties.getExchangesRetrieve(),
                properties.getRoutingKeyRetrieve(),
                GET_ALL_PRODUCTS);
    }

    @Override
    public ProductDTO insertProduct(ProductDTO product) {
        final ProductDTO productDTO = (ProductDTO) rabbitTemplate.convertSendAndReceive(properties.getExchangesInsertion(),
                properties.getRoutingKeyInsertion(),
                product);

        LOGGER.debug(productDTO);

        return productDTO;

    }
}
