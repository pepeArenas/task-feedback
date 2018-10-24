package com.ns.task.repositories;

import com.ns.task.entities.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long>, ProductRepositoryCustom {
    List<ProductEntity> findByNameAndModel(String name, String model);
    List<ProductEntity> findByNameContains (String name);


}
