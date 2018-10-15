package com.ns.task.product;

import com.mx.task.model.ProductDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private static final Logger logger = LogManager.getLogger();
    private static final List<ProductDTO> products;


    static {
        products = new ArrayList<>();
        products.add(new ProductDTO(1, true, "SCW090", "SCREWDRIVER", new BigDecimal("11.34"), true, null));
        products.add(new ProductDTO(2, true, "SCW091", "SCREWDRIVER", new BigDecimal("12.34"), true, null));
        products.add(new ProductDTO(3, true, "SCW092", "SCREWDRIVER", new BigDecimal("13.34"), true, null));
    }


    @Override
    public List<ProductDTO> getProducts() {
        return products;
    }

    @Override
    public void insertProduct(ProductDTO product) {
        logger.debug("Sending message to RabbitMQ {}.", product.toString());
        products.add(product);

    }
}
