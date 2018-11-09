package com.ns.task.repositories;

import com.ns.task.entities.ProductEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import java.util.List;

@Repository
public class ProductRepositoryImpl implements ProductRepositoryCustom {

    private static final Logger logger = LogManager.getLogger();


    @PersistenceContext
    private EntityManager manager;

    @Override
    public List<ProductEntity> retrieveProducts() {
        final StoredProcedureQuery retrieveProducts = manager.createNamedStoredProcedureQuery("findAllProducts");
        return retrieveProducts.getResultList();
    }

    @Override
    public ProductEntity saveProduct(ProductEntity product) throws DataIntegrityViolationException {
        final StoredProcedureQuery insertion = manager.createNamedStoredProcedureQuery("insertProduct");
        insertion.setParameter("productName", product.getName());
        insertion.setParameter("model", product.getModel());
        insertion.setParameter("price", product.getPrice());
        insertion.execute();
        logger.debug("Inserted service is {}", product);
        return product;
    }
}
