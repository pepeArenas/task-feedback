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
    private static final String RESPONSE_TOPIC = "t.resultado";

    @Autowired
    public void setKafkaTemplate(KafkaTemplate<String, ProductDTO> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Autowired
    public void setProductsTemplate(KafkaTemplate<String, ProductDTO[]> productsTemplate) {
        this.productsTemplate = productsTemplate;
    }

    private CountDownLatch latch = new CountDownLatch(1);

    @Override
    public List<ProductDTO> getProducts() {
        LOGGER.debug("Sending to kafka broker:");
        final ListenableFuture<SendResult<String, ProductDTO[]>> products = productsTemplate.send("t.get",
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
        final ProductDTO[] productDTO = {new ProductDTO()};
        final ListenableFuture<SendResult<String, ProductDTO>> productInserted = kafkaTemplate.send("t.insert",
                product);
        try {
            final SendResult<String, ProductDTO> future = productInserted.get();
            productDTO[0] = future.getProducerRecord().value();
        } catch (InterruptedException e) {
            LOGGER.error("An exception has occurred while thread was sleep {}", e);
        } catch (ExecutionException e) {
            LOGGER.error("An exception has occurred while asynchronous task block the thread {}", e);
        }
        return productDTO[0];

    }
}
