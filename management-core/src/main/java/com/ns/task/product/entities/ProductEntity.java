package com.ns.task.product.entities;

import com.ns.task.config.properties.CommonProperties;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Component
@Entity(name = "PRODUCT")
@NamedStoredProcedureQueries({
        @NamedStoredProcedureQuery(name = "findAllProducts", procedureName = "getAllProducts", resultClasses = {
                ProductEntity.class}),
        @NamedStoredProcedureQuery(name = "insertProduct", procedureName = "insertProd", parameters = {
                @StoredProcedureParameter(name = "productName", type = String.class, mode = ParameterMode.IN),
                @StoredProcedureParameter(name = "model", type = String.class, mode = ParameterMode.IN),
                @StoredProcedureParameter(name = "price", type = BigDecimal.class, mode = ParameterMode.IN),})})
public class ProductEntity {
    @Id
    @GeneratedValue
    private Integer id;
    @Column(name = "model")
    @Size(min = 3, max = 45, message = CommonProperties.INVALID_MODEL_SIZE)
    private String model;
    @Column(name = "name")
    @Size(min = 3, max = 45, message = CommonProperties.INVALID_NAME_SIZE)
    private String name;
    @Column(name = "price")
    @Min(value = 0L, message = CommonProperties.INVALID_POSITIVE_PRICE)
    @Digits(integer = 5, fraction = 2, message = CommonProperties.INVALID_PRICE_FORMAT)
    private BigDecimal price;
    @Transient
    private boolean isComplete;

    public ProductEntity() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void setComplete(boolean complete) {
        isComplete = complete;
    }

}
