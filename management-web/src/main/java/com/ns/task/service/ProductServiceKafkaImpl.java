package com.ns.task.service;

import com.ns.task.model.ProductDTO;
import com.ns.task.services.ProductService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;

@Service
@Profile("kafka")
public class ProductServiceKafkaImpl implements ProductService {
    private static final Logger LOGGER = LogManager.getLogger();
    private KafkaTemplate<String, ProductDTO> kafkaTemplate;
    private KafkaTemplate<String, ProductDTO[]> productsTemplate;
    private List<ProductDTO> productToReturn = new ArrayList<>();
    private ProductDTO productPersisted = new ProductDTO();
    private static final String RESPONSE_PRODUCTS_TOPIC = "t.products";
    private static final String RESPONSE_PRODUCT_TOPIC = "t.product";
    private CountDownLatch latchForGet = new CountDownLatch(1);
    private CountDownLatch latchForInsert = new CountDownLatch(1);

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
        ListenableFuture<SendResult<String, ProductDTO[]>> send = productsTemplate.send("t.get", new ProductDTO[0]);
        try {
            send.get();
            latchForGet.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return productToReturn;
    }

    @KafkaListener(topics = RESPONSE_PRODUCTS_TOPIC, containerFactory = "kafkaProductListenerContainerFactory")
    public ProductDTO[] readProductsFromTopic(ProductDTO[] products) {
        assignRetrieveProductsForView(products);
        latchForGet.countDown();
        return products;
    }

    private void assignRetrieveProductsForView(ProductDTO[] products) {
        LOGGER.debug("The number of products returned from Kakfa is: {}", products.length);
        //TODO synchronize
        productToReturn = Arrays.asList(products);
    }

    @Override
    public ProductDTO insertProduct(ProductDTO product) {
        productPersisted = new ProductDTO();
        //latchForInsert = new CountDownLatch(1);
        LOGGER.debug("Sending to kafka broker: {}", product);
        kafkaTemplate.send("t.insert", product);
        try {
            latchForInsert.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        latchForInsert.countDown();
        return productPersisted;
    }

    @KafkaListener(topics = RESPONSE_PRODUCT_TOPIC, containerFactory = "kafkaListenerContainerFactory")
    public ProductDTO readProductFromTopic(ProductDTO productPersisted) {
        assignRetrieveProductForView(productPersisted);
        return productPersisted;
    }

    private void assignRetrieveProductForView(ProductDTO product) {
        LOGGER.debug("The product persisted is: {}", product);
        productPersisted = product;

    }
}
