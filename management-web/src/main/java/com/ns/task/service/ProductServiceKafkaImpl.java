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

import java.util.Arrays;
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
        final ProductDTO[] productDTO = {new ProductDTO()};
        ListenableFuture<SendResult<String, ProductDTO>> products = kafkaTemplate.send("t.get",
                new ProductDTO());
        try {
            SendResult<String, ProductDTO> stringProductDTOSendResult = products.get();
            productDTO[0] = stringProductDTOSendResult.getProducerRecord().value();
            System.out.println(productDTO[0].getName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Arrays.asList(productDTO);
    }

    @Override
    public ProductDTO insertProduct(ProductDTO product) {
        logger.debug("Sending to kafka broker: {}", product);
        final ProductDTO[] productDTO = {new ProductDTO()};
        ListenableFuture<SendResult<String, ProductDTO>> productInserted = kafkaTemplate.send("t.insert",
                product);
        try {
            SendResult<String, ProductDTO> stringProductDTOSendResult = productInserted.get();
            productDTO[0] = stringProductDTOSendResult.getProducerRecord().value();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return productDTO[0];

    }
}
