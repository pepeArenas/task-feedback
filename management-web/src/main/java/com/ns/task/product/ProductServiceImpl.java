package com.ns.task.product;

import com.mx.task.model.ProductDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Service
public class ProductServiceImpl implements ProductService {

    private static final Logger LOGGER = Logger.getLogger(ProductServiceImpl.class.getName());

    @Override
    public List<ProductDTO> getProducts() {
        LOGGER.info("inside getProduct()");
        return new ArrayList<>();//TODO This will change when integration with RabbitMQ
    }

    @Override
    public void insertProduct(ProductDTO product) {
        LOGGER.info("calling insertProduct with params");
    }
}
