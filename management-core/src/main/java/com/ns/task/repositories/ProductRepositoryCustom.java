package com.ns.task.repositories;

import com.ns.task.entities.ProductEntity;

import java.util.List;

public interface ProductRepositoryCustom {
    List<ProductEntity> retrieveProducts();

    ProductEntity saveProduct(ProductEntity product);
}
