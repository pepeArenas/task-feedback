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

    private static Logger logger = LogManager.getLogger();
    private static List<ProductDTO> products;


    static {
        products = new ArrayList<>();
        products.add(new ProductDTO(1, true, "SCW090", "SCREWER", new BigDecimal("11.34"), true, null));
        products.add(new ProductDTO(2, true, "SCW091", "SCREWER", new BigDecimal("12.34"), true, null));
        products.add(new ProductDTO(3, true, "SCW092", "SCREWER", new BigDecimal("13.34"), true, null));
    }


    @Override
    public List<ProductDTO> getProducts() {
        logger.debug("Mensaje debug ejemplo");
        logger.info("Mensaje info ejemplo");
        logger.warn("Mensaje warn ejemplo");
        logger.error("Mensaje error ejemplo");
        logger.fatal("Mensaje fatal ejemplo");
        return products;//TODO This will change when integration with RabbitMQ
    }

    @Override
    public void insertProduct(ProductDTO product) {
        logger.info("Mensaje de ejemplo insert");
        products.add(product);

    }
}
