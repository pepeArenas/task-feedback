package com.ns.task.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ns.task.config.Producer;
import com.ns.task.model.ProductDTO;
import com.ns.task.services.ProductService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private static final Logger logger = LogManager.getLogger();
    private static final List<ProductDTO> products;
    private Producer producer;


    static {
        products = new ArrayList<>();
        products.add(new ProductDTO(1, "SCW090", "SCREWDRIVER", new BigDecimal("11.34")));
        products.add(new ProductDTO(2, "SCW091", "SCREWDRIVER", new BigDecimal("12.34")));
        products.add(new ProductDTO(3, "SCW092", "SCREWDRIVER", new BigDecimal("13.34")));
    }

    @Autowired
    public ProductServiceImpl(Producer producer) {
        this.producer = producer;
    }


    @Override
    public List<ProductDTO> getProducts() {
        return products;
    }

    @Override
    public ProductDTO insertProduct(ProductDTO product) {
        try {
            producer.produce(product);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return product;

    }
}
