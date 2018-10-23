package com.ns.task.repositories;

import com.ns.task.entities.ProductEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import java.util.List;

@Repository
public class ProductRepositoryImpl implements ProductRepositoryCustom {

    @PersistenceContext
    private EntityManager manager;

    @Override
    public List<ProductEntity> retrieveProducts() {
        StoredProcedureQuery retrieveProducts = manager.createNamedStoredProcedureQuery("findAllProducts");
        return retrieveProducts.getResultList();
    }

    @Override
    public ProductEntity saveProduct(ProductEntity product) {
        StoredProcedureQuery insertion = manager.createNamedStoredProcedureQuery("insertProduct");
        insertion.setParameter("productName", product.getName());
        insertion.setParameter("model", product.getModel());
        insertion.setParameter("price", product.getPrice());
        insertion.execute();
        return product;
    }
}
