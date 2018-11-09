package com.ns.task.model;

import com.ns.task.config.properties.CommonProperties;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.math.BigDecimal;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
        final ProductDTO product = new ProductDTOBuilder()
                .setName("Screwdriver")
                .setModel("A083434")
                .setPrice(new BigDecimal("12.3"))
                .createProductDTO();
        // when:
        final Set<ConstraintViolation<ProductDTO>> violations = validator.validate(product);
        // then:
        assertTrue(violations.isEmpty());
    }

    @Test
    public void productShouldDetectInvalidMinNameSize() {
        // given:
        final ProductDTO product = new ProductDTOBuilder()
                .setName("De")
                .setModel("A083434")
                .setPrice(new BigDecimal("12.9"))
                .createProductDTO();
        // when:
        final Set<ConstraintViolation<ProductDTO>> violations = validator.validate(product);
        // then:
        assertEquals(violations.size(), 1);
        final ConstraintViolation<ProductDTO> violation = violations.iterator().next();
        assertEquals(CommonProperties.INVALID_NAME_SIZE, violation.getMessage());
    }

    @Test
    public void productShouldDetectInvalidMaxNameSize() {
        // given:
        final ProductDTO product = new ProductDTOBuilder()
                .setName("1234567890123456789012345678901234567890123456")
                .setModel("A083434")
                .setPrice(new BigDecimal("12.9"))
                .createProductDTO();
        // when:
        final Set<ConstraintViolation<ProductDTO>> violations = validator.validate(product);
        // then:
        assertEquals(violations.size(), 1);
        final ConstraintViolation<ProductDTO> violation = violations.iterator().next();
        assertEquals(CommonProperties.INVALID_NAME_SIZE, violation.getMessage());
    }

    @Test
    public void productShouldDetectInvalidMinModelSize() {
        // given:
        final ProductDTO product = new ProductDTOBuilder()
                .setName("Screwdriver")
                .setModel("A0")
                .setPrice(new BigDecimal("12.9"))
                .createProductDTO();
        // when:
        final Set<ConstraintViolation<ProductDTO>> violations = validator.validate(product);
        // then:
        assertEquals(violations.size(), 1);
        final ConstraintViolation<ProductDTO> violation = violations.iterator().next();
        assertEquals(CommonProperties.INVALID_MODEL_SIZE, violation.getMessage());
    }

    @Test
    public void productShouldDetectInvalidMaxModelSize() {
        // given:
        final ProductDTO product = new ProductDTOBuilder()
                .setName("Screwdriver")
                .setModel("1234567890123456789012345678901234567890123456")
                .setPrice(new BigDecimal("12.9"))
                .createProductDTO();
        // when:
        final Set<ConstraintViolation<ProductDTO>> violations = validator.validate(product);
        // then:
        assertEquals(violations.size(), 1);
        final ConstraintViolation<ProductDTO> violation = violations.iterator().next();
        assertEquals(CommonProperties.INVALID_MODEL_SIZE, violation.getMessage());
    }

    @Test
    public void productShouldDetectInvalidPriceSize() {
        // given:
        final ProductDTO product = new ProductDTOBuilder()
                .setName("Screwdriver")
                .setModel("A083434")
                .setPrice(new BigDecimal("123456.92"))
                .createProductDTO();
        // when:
        final Set<ConstraintViolation<ProductDTO>> violations = validator.validate(product);
        // then:
        assertEquals(violations.size(), 1);
        final ConstraintViolation<ProductDTO> violation = violations.iterator().next();
        assertEquals(CommonProperties.INVALID_PRICE_FORMAT, violation.getMessage());
    }

    @Test
    public void productShouldDetectInvalidPositivePrice() {
        // given:
        final ProductDTO product = new ProductDTOBuilder()
                .setName("Screwdriver")
                .setModel("A083434")
                .setPrice(new BigDecimal("-1"))
                .createProductDTO();
        // when:
        final Set<ConstraintViolation<ProductDTO>> violations = validator.validate(product);
        // then:
        assertEquals(violations.size(), 1);
        final ConstraintViolation<ProductDTO> violation = violations.iterator().next();
        assertEquals(CommonProperties.INVALID_POSITIVE_PRICE, violation.getMessage());
    }

}
