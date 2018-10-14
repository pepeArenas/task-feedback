package com.mx.task.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("management-web")
public class CommonProperties {
    public static final String INVALID_NAME_SIZE = "Name should be between 3 and 45 characters";
    public static final String INVALID_MODEL_SIZE = "Model should be between 3 and 45 characters";
    public static final String INVALID_PRICE_FORMAT = "Price should be 5 integer tops and 2 decimal";
    public static final String INVALID_POSIVE_PRICE = "The value must be positive";

}
