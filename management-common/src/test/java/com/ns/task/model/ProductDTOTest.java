package com.ns.task.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ns.task.config.properties.CommonProperties;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.io.IOException;
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
        ObjectMapper mapper = new ObjectMapper();
        // given:
        ProductDTO product = new ProductDTO();
        product.setName("Screwdriver");
        product.setModel("A083434");
        product.setPrice(new BigDecimal("12.3"));
        // when:
        Set<ConstraintViolation<ProductDTO>> violations = validator.validate(product);
        // then:
        assertTrue(violations.isEmpty());
        try {
            String a = mapper.writeValueAsString(product);

            String JSON = "{\"id\":null,\"model\":\"TRE345\",\"name\":\"TRACKTOR\",\"price\":22,\"message\":null,\"complete\":false}";
            ProductDTO productDTO = mapper.readValue(JSON, ProductDTO.class);

            System.out.println(productDTO);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        Assert.assertEquals(CommonProperties.INVALID_NAME_SIZE, violation.getMessage());
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
        assertEquals(CommonProperties.INVALID_NAME_SIZE, violation.getMessage());
    }

    @Test
    public void productShouldDetectInvalidMinModelSize() {
        // given:
        ProductDTO product = new ProductDTO();
        product.setName("Screwdriver");
        product.setModel("A0");
        product.setPrice(new BigDecimal("12.9"));
        // when:
        Set<ConstraintViolation<ProductDTO>> violations = validator.validate(product);
        // then:
        assertEquals(violations.size(), 1);
        ConstraintViolation<ProductDTO> violation = violations.iterator().next();
        assertEquals(CommonProperties.INVALID_MODEL_SIZE, violation.getMessage());
    }

    @Test
    public void productShouldDetectInvalidMaxModelSize() {
        // given:
        ProductDTO product = new ProductDTO();
        product.setName("Screwdriver");
        product.setModel("1234567890123456789012345678901234567890123456");
        product.setPrice(new BigDecimal("12.9"));
        // when:
        Set<ConstraintViolation<ProductDTO>> violations = validator.validate(product);
        // then:
        assertEquals(violations.size(), 1);
        ConstraintViolation<ProductDTO> violation = violations.iterator().next();
        assertEquals(CommonProperties.INVALID_MODEL_SIZE, violation.getMessage());
    }

    @Test
    public void productShouldDetectInvalidPriceSize() {
        // given:
        ProductDTO product = new ProductDTO();
        product.setName("Screwdriver");
        product.setModel("A083434");
        product.setPrice(new BigDecimal("123456.92"));
        // when:
        Set<ConstraintViolation<ProductDTO>> violations = validator.validate(product);
        // then:
        assertEquals(violations.size(), 1);
        ConstraintViolation<ProductDTO> violation = violations.iterator().next();
        assertEquals(CommonProperties.INVALID_PRICE_FORMAT, violation.getMessage());
    }

    @Test
    public void productShouldDetectInvalidPositivePrice() {
        // given:
        ProductDTO product = new ProductDTO();
        product.setName("Screwdriver");
        product.setModel("A083434");
        product.setPrice(new BigDecimal("-1"));
        // when:
        Set<ConstraintViolation<ProductDTO>> violations = validator.validate(product);
        // then:
        assertEquals(violations.size(), 1);
        ConstraintViolation<ProductDTO> violation = violations.iterator().next();
        assertEquals(CommonProperties.INVALID_POSITIVE_PRICE, violation.getMessage());
    }

}
