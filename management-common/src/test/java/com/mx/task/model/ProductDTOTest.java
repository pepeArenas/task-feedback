package com.mx.task.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class ProductDTOTest {
    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @BeforeClass
    public static void createValidator() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterClass
    public static void close() {
        validatorFactory.close();
    }

    @Test
    public void productShouldNoViolations() {
        // given:
        ProductDTO product = new ProductDTO();
        product.setName("Desarmador");
        product.setModel("A083434");
        product.setPrice(new BigDecimal("12.3"));
        // when:
        Set<ConstraintViolation<ProductDTO>> violations = validator.validate(product);
        // then:
        assertTrue(violations.isEmpty());
    }

    @Test
    public void productShouldDetectInvalidMinNameSize() {
        // given:
        ProductDTO product = new ProductDTO();
        product.setName("De");
        product.setModel("A083434");
        product.setPrice(new BigDecimal("12.9"));
        // when:
        Set<ConstraintViolation<ProductDTO>> violations = validator.validate(product);
        // then:
        assertEquals(violations.size(), 1);
        ConstraintViolation<ProductDTO> violation = violations.iterator().next();
        assertEquals("Name should be between 3 and 45 characters", violation.getMessage());
    }

    @Test
    public void productShouldDetectInvalidMaxNameSize() {
        // given:
        ProductDTO product = new ProductDTO();
        product.setName("1234567890123456789012345678901234567890123456");
        product.setModel("A083434");
        product.setPrice(new BigDecimal("12.9"));
        // when:
        Set<ConstraintViolation<ProductDTO>> violations = validator.validate(product);
        // then:
        assertEquals(violations.size(), 1);
        ConstraintViolation<ProductDTO> violation = violations.iterator().next();
        assertEquals("Name should be between 3 and 45 characters", violation.getMessage());
    }

    @Test
    public void productShouldDetectInvalidMinModelSize() {
        // given:
        ProductDTO product = new ProductDTO();
        product.setName("Desarmador");
        product.setModel("A0");
        product.setPrice(new BigDecimal("12.9"));
        // when:
        Set<ConstraintViolation<ProductDTO>> violations = validator.validate(product);
        // then:
        assertEquals(violations.size(), 1);
        ConstraintViolation<ProductDTO> violation = violations.iterator().next();
        assertEquals("Model should be between 3 and 45 characters", violation.getMessage());
    }

    @Test
    public void productShouldDetectInvalidMaxModelSize() {
        // given:
        ProductDTO product = new ProductDTO();
        product.setName("Desarmador");
        product.setModel("1234567890123456789012345678901234567890123456");
        product.setPrice(new BigDecimal("12.9"));
        // when:
        Set<ConstraintViolation<ProductDTO>> violations = validator.validate(product);
        // then:
        assertEquals(violations.size(), 1);
        ConstraintViolation<ProductDTO> violation = violations.iterator().next();
        assertEquals("Model should be between 3 and 45 characters", violation.getMessage());
    }

    @Test
    public void productShouldDetectInvalidPriceSize() {
        // given:
        ProductDTO product = new ProductDTO();
        product.setName("Desarmador");
        product.setModel("A083434");
        product.setPrice(new BigDecimal("123456.92"));
        // when:
        Set<ConstraintViolation<ProductDTO>> violations = validator.validate(product);
        // then:
        assertEquals(violations.size(), 1);
        ConstraintViolation<ProductDTO> violation = violations.iterator().next();
        assertEquals("Price shoud be 5 integer tops and 2 decimal", violation.getMessage());
    }

    @Test
    public void productShouldDetectInvalidPositivePrice() {
        // given:
        ProductDTO product = new ProductDTO();
        product.setName("Desarmador");
        product.setModel("A083434");
        product.setPrice(new BigDecimal("-1"));
        // when:
        Set<ConstraintViolation<ProductDTO>> violations = validator.validate(product);
        // then:
        assertEquals(violations.size(), 1);
        ConstraintViolation<ProductDTO> violation = violations.iterator().next();
        assertEquals("The value must be positive", violation.getMessage());
    }

}
