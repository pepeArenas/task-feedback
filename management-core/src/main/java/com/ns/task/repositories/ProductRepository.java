package com.ns.task.repositories;

import com.ns.task.entities.ProductEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends CrudRepository<ProductEntity, Long>, ProductRepositoryCustom {
}
