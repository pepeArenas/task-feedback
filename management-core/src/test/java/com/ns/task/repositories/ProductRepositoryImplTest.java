package com.ns.task.repositories;

import com.ns.task.entities.ProductEntity;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
public class ProductRepositoryImplTest {

    @Autowired
    private ProductRepository repository;
    private ProductEntity hammerTest;
    private ProductEntity screwdriverTest;

    @Before
    public void setUp() {
        hammerTest = new ProductEntity();
        hammerTest.setName("TEST_HAMMER");
        hammerTest.setModel("TEST_H090");
        hammerTest.setPrice(new BigDecimal("12.90"));
        screwdriverTest = new ProductEntity();
        screwdriverTest.setName("TEST_SCREWDRIVER");
        screwdriverTest.setModel("TEST_S090");
        screwdriverTest.setPrice(new BigDecimal("12.90"));
    }

    @Test
    public void saveProduct() throws SQLIntegrityConstraintViolationException {
        ProductEntity productSaved = repository.saveProduct(hammerTest);
        List<ProductEntity> productsReturned = repository.findByNameAndModel(
                productSaved.getName(),
                productSaved.getModel());
        Assertions.assertThat(productsReturned.isEmpty()).isFalse();
    }

    @Test
    public void shouldSaveProductSameNameDifferentModel() throws SQLIntegrityConstraintViolationException {
        screwdriverTest.setName("TEST_SCREWDRIVER");
        screwdriverTest.setModel("TEST_S090");
        screwdriverTest.setPrice(new BigDecimal("12.90"));
        ProductEntity sameNameAndModel = new ProductEntity();
        sameNameAndModel.setName("TEST_SCREWDRIVER2");
        sameNameAndModel.setModel("TEST_S090");
        sameNameAndModel.setPrice(new BigDecimal("12.90"));
        repository.saveProduct(screwdriverTest);
        repository.saveProduct(sameNameAndModel);
        List<ProductEntity> productsReturned = repository.findByNameContains(
                "TEST_");
        Assertions.assertThat(productsReturned.isEmpty()).isFalse();
        Assertions.assertThat(productsReturned.size()).isEqualTo(2);

    }

    @Test
    public void retrieveAllProduct() throws SQLIntegrityConstraintViolationException {
        repository.saveProduct(hammerTest);
        repository.saveProduct(screwdriverTest);
        List<ProductEntity> productReturnedFormDB = repository.retrieveProducts();
        Assertions.assertThat(productReturnedFormDB.isEmpty()).isFalse();
    }

    @Test
    public void trySaveDuplicateProduct() {
        try {
            repository.saveProduct(hammerTest);
            repository.saveProduct(hammerTest);
        } catch (Exception exception) {
            Assertions.assertThat(exception instanceof SQLIntegrityConstraintViolationException);
        }
    }
}