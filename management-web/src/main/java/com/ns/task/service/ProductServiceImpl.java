package com.ns.task.service;

import com.ns.task.config.properties.BrokerProperties;
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
    private final RabbitTemplate rabbitTemplate;
    private final BrokerProperties properties;

    @Autowired
    public ProductServiceImpl(RabbitTemplate rabbitTemplate, BrokerProperties properties) {
        this.rabbitTemplate = rabbitTemplate;
        this.properties = properties;
    }

    @Override
    public List<ProductDTO> getProducts() {
        return (List<ProductDTO>) rabbitTemplate.convertSendAndReceive(properties.getExchangesRetrieve(),
                properties.getRoutingKeyRetrieve(),
                "getAllProducts");
    }

    @Override
    public ProductDTO insertProduct(ProductDTO product) {
        final ProductDTO productDTO = (ProductDTO) rabbitTemplate.convertSendAndReceive(properties.getExchangesInsertion(),
                properties.getRoutingKeyInsertion(),
                product);

        logger.debug(productDTO);

        return productDTO;

    }
}
