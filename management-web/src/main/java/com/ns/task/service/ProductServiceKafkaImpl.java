package com.ns.task.service;

import com.ns.task.model.ProductDTO;
import com.ns.task.services.ProductService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;

@Service
@Profile("kafka")
public class ProductServiceKafkaImpl implements ProductService {
    private static final Logger LOGGER = LogManager.getLogger();
    private KafkaTemplate<String, ProductDTO> kafkaTemplate;
    private KafkaTemplate<String, ProductDTO[]> productsTemplate;
    private List<ProductDTO> productToReturn = new ArrayList<>();
    private ProductDTO productPersisted;
    private static final String RESPONSE_TOPIC = "t.resultado";
    private static final String RESPONSE_PRODUCT_TOPIC = "t.producto";
    private CountDownLatch latch = new CountDownLatch(1);
    private CountDownLatch latchProduct = new CountDownLatch(1);

    @Autowired
    public void setKafkaTemplate(KafkaTemplate<String, ProductDTO> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Autowired
    public void setProductsTemplate(KafkaTemplate<String, ProductDTO[]> productsTemplate) {
        this.productsTemplate = productsTemplate;
    }


    @Override
    public List<ProductDTO> getProducts() {
        LOGGER.debug("Sending to kafka broker:");
        productsTemplate.send("t.get",
                new ProductDTO[0]);
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return productToReturn;
    }

    @KafkaListener(topics = RESPONSE_TOPIC, containerFactory = "kafkaProductListenerContainerFactory")
    public ProductDTO[] readProductsFromTopic(ProductDTO[] products) {
        assignRetrieveProductsForView(products);
        latch.countDown();
        return products;
    }

    private void assignRetrieveProductsForView(ProductDTO[] products) {
        LOGGER.debug("The number of products returned from Kakfa is: {}", products.length);
        //TODO synchronize
        productToReturn = Arrays.asList(products);
    }

    @Override
    public ProductDTO insertProduct(ProductDTO product) {
        LOGGER.debug("Sending to kafka broker: {}", product);
        kafkaTemplate.send("t.insert",
                product);

        try {
            latchProduct.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return productPersisted;

    }

    @KafkaListener(topics = RESPONSE_PRODUCT_TOPIC, containerFactory = "kafkaListenerContainerFactory")
    public ProductDTO readProductsFromTopic(ProductDTO products) {
        LOGGER.debug("The product returned from Kakfa is: {}", products);
        assignRetrieveProductForView(products);
        latchProduct.countDown();
        return products;
    }

    private void assignRetrieveProductForView(ProductDTO products) {
        //TODO synchronize
        productPersisted = products;
    }
}
