package com.ns.task.config.properties.kafka;

import com.ns.task.model.ProductDTO;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

//@Configuration
//@Profile("kafka")
public class KafkaConfiguration {
    private static final String BOOTSTRAP_ADDRESS = "localhost:9092";
    private final Map<String, Object> configProps;

    public KafkaConfiguration() {
        configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_ADDRESS);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
    }

    @Bean
    public ProducerFactory<String, ProductDTO> greetingProducerFactory() {
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, ProductDTO> greetingKafkaTemplate() {
        return new KafkaTemplate<>(greetingProducerFactory());
    }

    @Bean
    public ProducerFactory<String, ProductDTO[]> producerProductsFactory() {
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, ProductDTO[]> productsKafkaTemplate() {
        return new KafkaTemplate<>(producerProductsFactory());
    }
}
