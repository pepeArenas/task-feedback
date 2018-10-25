package com.ns.task.repositories;

import com.ns.task.entities.ProductEntity;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;

public interface ProductRepositoryCustom {
    List<ProductEntity> retrieveProducts();

    ProductEntity saveProduct(ProductEntity product) throws DataIntegrityViolationException;
}
