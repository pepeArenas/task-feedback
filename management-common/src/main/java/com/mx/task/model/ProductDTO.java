package com.mx.task.model;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;

public class ProductDTO implements Serializable {
    private Integer id;
    private boolean isCorrect;
    @Size(min = 3, max = 45, message = "Model should be between 3 and 45 characters")
    private String model;
    @Size(min = 3, max = 45, message = "Name should be between 3 and 45 characters")
    private String name;
    @Min(value = 0L, message = "The value must be positive")
    @Digits(integer = 5, fraction = 2, message = "Price shoud be 5 integer tops and 2 decimal")
    private BigDecimal price;
    private boolean isComplete;
    private MetaDTO details;

    public ProductDTO() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
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

    public MetaDTO getDetails() {
        return details;
    }

    public void setDetails(MetaDTO details) {
        this.details = details;
    }
}
