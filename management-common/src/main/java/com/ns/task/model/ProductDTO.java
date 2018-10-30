package com.ns.task.model;

import com.ns.task.config.properties.CommonProperties;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.StringJoiner;

public class ProductDTO implements Serializable {
    private Integer id;
    @NotNull
    @Size(min = 3, max = 45, message = CommonProperties.INVALID_MODEL_SIZE)
    private String model;
    @NotNull
    @Size(min = 3, max = 45, message = CommonProperties.INVALID_NAME_SIZE)
    private String name;
    @NotNull
    @Min(value = 0L, message = CommonProperties.INVALID_POSITIVE_PRICE)
    @Digits(integer = 5, fraction = 2, message = CommonProperties.INVALID_PRICE_FORMAT)
    private BigDecimal price;


    public ProductDTO() {
    }

    public ProductDTO(Integer id, String model, String name, BigDecimal price) {
        this.id = id;
        this.model = model;
        this.name = name;
        this.price = price;
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


    @Override
    public String toString() {
        return new StringJoiner(", ", ProductDTO.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("model='" + model + "'")
                .add("name='" + name + "'")
                .add("price=" + price)
                .toString();
    }
}
