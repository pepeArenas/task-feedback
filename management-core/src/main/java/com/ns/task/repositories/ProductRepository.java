package com.ns.task.repositories;

import com.ns.task.entities.ProductEntity;
import com.ns.task.exceptions.ProductManagementException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long>, ProductRepositoryCustom {
    List<ProductEntity> findByNameAndModelAndPrice(String name, String model, BigDecimal price);

    List<ProductEntity> findByNameContainsOrderByNameAsc(String name);

    List<ProductEntity> retrieveProducts();

    ProductEntity saveProduct(ProductEntity product) throws ProductManagementException;


}
