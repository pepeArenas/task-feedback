package com.ns.task.config.properties.kafka;

import com.ns.task.model.ProductDTO;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@Profile("kafka")
public class KafkaConfiguration {
    @Bean
    public ProducerFactory<String, ProductDTO> greetingProducerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        String bootstrapAddress = "localhost:9092";
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, ProductDTO> greetingKafkaTemplate() {
        return new KafkaTemplate<>(greetingProducerFactory());
    }
}
