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
import java.util.concurrent.TimeUnit;

@Service
@Profile("kafka")
public class ProductServiceKafkaImpl implements ProductService {
    private static final Logger LOGGER = LogManager.getLogger();
    private KafkaTemplate<String, ProductDTO> kafkaTemplate;
    private KafkaTemplate<String, ProductDTO[]> productsTemplate;
    private List<ProductDTO> productToReturn = new ArrayList<>();
    private ProductDTO productPersisted;
    private static final String RESPONSE_PRODUCTS_TOPIC = "t.products";
    private static final String RESPONSE_PRODUCT_TOPIC = "t.product";
    private CountDownLatch latch = new CountDownLatch(1);
    private CountDownLatch latchProduct;
    private final static Object lock = new Object();
    private boolean isServiceDependantAvailable;

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
        productToReturn = new ArrayList<>();
        latch = new CountDownLatch(1);
        LOGGER.debug("Sending to kafka broker:");
        productsTemplate.send("t.get",
                new ProductDTO[0]);
        try {
            latch.await(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        LOGGER.debug("READY FOR RETURN TO VIEW");
        return productToReturn;
    }

    @KafkaListener(topics = RESPONSE_PRODUCTS_TOPIC, containerFactory = "kafkaProductListenerContainerFactory")
    public ProductDTO[] readProductsFromTopic(ProductDTO[] products) {
        assignRetrieveProductsForView(products);
        latch.countDown();
        return products;
    }

    private void assignRetrieveProductsForView(ProductDTO[] products) {
        LOGGER.debug("The number of products returned from Kakfa is: {}", products.length);
        productToReturn = Arrays.asList(products);
    }


    @Override
    public ProductDTO insertProduct(ProductDTO product) {
        synchronized (lock) {
            LOGGER.error(isServiceDependantAvailable);
            productPersisted = new ProductDTO();
            latchProduct = new CountDownLatch(1);
            LOGGER.debug("Sending to kafka broker: {}", product);
            kafkaTemplate.send("t.insert", product);
            try {
                latchProduct.await(10, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                LOGGER.error("An exception has occurred while thread was sleep {}", e);
            }
            LOGGER.debug("READY FOR RETURN TO VIEW");
            checkIfServiceIsAvailable();
        }
        return productPersisted;
    }

    private void checkIfServiceIsAvailable() {
        if (!isServiceDependantAvailable) {
            productPersisted.setMessage("Service takes more time to respond probably unavailable");
            LOGGER.error("Service takes more time to respond probably unavailable {}", productPersisted.getMessage());
        }
    }

    @KafkaListener(topics = RESPONSE_PRODUCT_TOPIC, containerFactory = "kafkaListenerContainerFactory")
    public ProductDTO readProductsFromTopic(ProductDTO products) {
        LOGGER.debug("The prod  uct returned from Kakfa is: {}", products);
        assignRetrieveProductForView(products);
        latchProduct.countDown();
        isServiceDependantAvailable = true;
        return products;
    }

    private void assignRetrieveProductForView(ProductDTO products) {
        productPersisted = products;
    }
}
