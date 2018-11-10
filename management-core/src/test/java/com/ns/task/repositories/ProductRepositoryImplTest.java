package com.ns.task.repositories;

import com.ns.task.entities.ProductEntity;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
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
    public void saveProduct() {
        final ProductEntity productSaved = repository.saveProduct(hammerTest);
        final List<ProductEntity> productsReturned = repository.findByNameAndModelAndPrice(
                productSaved.getName(),
                productSaved.getModel(),
                productSaved.getPrice());
        Assertions.assertThat(productsReturned.isEmpty()).isFalse();
        Assertions.assertThat(productsReturned.size()).isEqualTo(1);
        validateContentOfFieldsAreNotNull(productsReturned.get(0));
        validateContentOfFieldsAreEqualsBeforeAndAfterSave(hammerTest, productsReturned.get(0));
    }

    @Test
    public void shouldSaveProductSameNameDifferentModel() {
        final ProductEntity sameNameAndModel = new ProductEntity();
        sameNameAndModel.setName("TEST_SCREWDRIVER2");
        sameNameAndModel.setModel("TEST_S090");
        sameNameAndModel.setPrice(new BigDecimal("12.90"));
        repository.saveProduct(screwdriverTest);
        repository.saveProduct(sameNameAndModel);
        final List<ProductEntity> productsReturned = repository.findByNameContainsOrderByNameAsc("TEST_");
        Assertions.assertThat(productsReturned.isEmpty()).isFalse();
        Assertions.assertThat(productsReturned.size()).isEqualTo(2);
        validateContentOfFieldsAreNotNull(productsReturned.get(0));
        validateContentOfFieldsAreNotNull(productsReturned.get(1));
        validateContentOfFieldsAreEqualsBeforeAndAfterSave(screwdriverTest, productsReturned.get(0));
        validateContentOfFieldsAreEqualsBeforeAndAfterSave(sameNameAndModel, productsReturned.get(1));
    }

    @Test
    public void retrieveAllProduct() {
        repository.saveProduct(hammerTest);
        repository.saveProduct(screwdriverTest);
        final List<ProductEntity> productReturnedFormDB = repository.findByNameContainsOrderByNameAsc("TEST_");
        Assertions.assertThat(productReturnedFormDB.isEmpty()).isFalse();
        Assertions.assertThat(productReturnedFormDB.size()).isEqualTo(2);
        validateContentOfFieldsAreNotNull(productReturnedFormDB.get(0));
        validateContentOfFieldsAreNotNull(productReturnedFormDB.get(1));
        validateContentOfFieldsAreEqualsBeforeAndAfterSave(hammerTest, productReturnedFormDB.get(0));
        validateContentOfFieldsAreEqualsBeforeAndAfterSave(screwdriverTest, productReturnedFormDB.get(1));
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void trySaveDuplicateProduct() throws DataIntegrityViolationException {
        repository.saveProduct(hammerTest);
        repository.saveProduct(hammerTest);
    }

    private void validateContentOfFieldsAreNotNull(ProductEntity product) {
        Assertions.assertThat(product.getId()).isNotNull();
        Assertions.assertThat(product.getName()).isNotNull();
        Assertions.assertThat(product.getModel()).isNotNull();
        Assertions.assertThat(product.getPrice()).isNotNull();
    }

    private void validateContentOfFieldsAreEqualsBeforeAndAfterSave(
            ProductEntity productAfterSaved,
            ProductEntity productBeforeSaved) {
        Assertions.assertThat(productAfterSaved.getName()).isEqualTo(productBeforeSaved.getName());
        Assertions.assertThat(productAfterSaved.getModel()).isEqualTo(productBeforeSaved.getModel());
        Assertions.assertThat(productAfterSaved.getPrice()).isEqualTo(productBeforeSaved.getPrice());
    }
}