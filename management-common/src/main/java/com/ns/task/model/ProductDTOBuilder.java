package com.ns.task.model;

import java.math.BigDecimal;

public class ProductDTOBuilder {
    private Integer id;
    private String model;
    private String name;
    private BigDecimal price;
    private String message;

    public ProductDTOBuilder setId(Integer id) {
        this.id = id;
        return this;
    }

    public ProductDTOBuilder setModel(String model) {
        this.model = model;
        return this;
    }

    public ProductDTOBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public ProductDTOBuilder setPrice(BigDecimal price) {
        this.price = price;
        return this;
    }

    public ProductDTOBuilder setMessage(String message) {
        this.message = message;
        return this;
    }

    public ProductDTO createProductDTO() {
        return new ProductDTO(id, model, name, price, message);
    }
}