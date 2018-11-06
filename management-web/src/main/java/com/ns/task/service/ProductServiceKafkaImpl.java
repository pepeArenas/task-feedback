package com.ns.task.service;

import com.ns.task.model.ProductDTO;
import com.ns.task.services.ProductService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.List;

@Service
@Profile("kafka")
public class ProductServiceKafkaImpl implements ProductService {
    private static final Logger logger = LogManager.getLogger();
    private KafkaTemplate<String, ProductDTO> kafkaTemplate;

    @Autowired
    public void setKafkaTemplate(KafkaTemplate<String, ProductDTO> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public List<ProductDTO> getProducts() {
        logger.debug("Sending to kafka broker:");
        ListenableFuture<SendResult<String, ProductDTO>> products = kafkaTemplate.send("t.get",
                new ProductDTO());
        products.addCallback(new ListenableFutureCallback<SendResult<String, ProductDTO>>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(SendResult<String, ProductDTO> result) {

                System.err.println(result.getProducerRecord().value());

            }
        });

        return null;
    }

    @Override
    public ProductDTO insertProduct(ProductDTO product) {
        logger.debug("Sending to kafka broker: {}", product);
        final ProductDTO[] productDTO = {new ProductDTO()};
        ListenableFuture<SendResult<String, ProductDTO>> productInserted = kafkaTemplate.send("t.insert",
                product);
        productInserted.addCallback(new ListenableFutureCallback<SendResult<String, ProductDTO>>() {

            @Override
            public void onSuccess(SendResult<String, ProductDTO> result) {
                productDTO[0] = result.getProducerRecord().value();
            }

            @Override
            public void onFailure(Throwable ex) {
                System.err.println(ex);
            }

        });

        return productDTO[0];

    }
}
