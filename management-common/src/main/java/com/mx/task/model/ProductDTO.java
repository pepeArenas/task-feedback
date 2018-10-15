package com.mx.task.model;

import com.mx.task.config.properties.CommonProperties;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.StringJoiner;

public class ProductDTO {
    private Integer id;
    private boolean isCorrect;
    @Size(min = 3, max = 45, message = CommonProperties.INVALID_MODEL_SIZE)
    private String model;
    @Size(min = 3, max = 45, message = CommonProperties.INVALID_NAME_SIZE)
    private String name;
    @Min(value = 0L, message = CommonProperties.INVALID_POSITIVE_PRICE)
    @Digits(integer = 5, fraction = 2, message = CommonProperties.INVALID_PRICE_FORMAT)
    private BigDecimal price;
    private boolean isComplete;
    private MetadataDTO metadata;

    public ProductDTO() {
    }

    public ProductDTO(Integer id, boolean isCorrect, String model, String name, BigDecimal price, boolean isComplete, MetadataDTO metadata) {
        this.id = id;
        this.isCorrect = isCorrect;
        this.model = model;
        this.name = name;
        this.price = price;
        this.isComplete = isComplete;
        this.metadata = metadata;
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

    public MetadataDTO getDetails() {
        return metadata;
    }

    public void setDetails(MetadataDTO meta) {
        this.metadata = meta;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ProductDTO.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("isCorrect=" + isCorrect)
                .add("model='" + model + "'")
                .add("name='" + name + "'")
                .add("price=" + price)
                .add("isComplete=" + isComplete)
                .add("metadata=" + metadata)
                .toString();
    }
}
