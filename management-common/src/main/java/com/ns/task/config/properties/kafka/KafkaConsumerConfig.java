package com.ns.task.config.properties.kafka;


import com.ns.task.model.ProductDTO;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
@Profile("kafka")
public class KafkaConsumerConfig {
    private Map<String, Object> props = new HashMap<>();
    private static final String BOOTSTRAP_ADDRESS = "localhost:9092";
    private static final String GROUP_ID = "group_insert";

    @Bean
    public Map<String, Object> consumerProps() {
        props.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_ADDRESS);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return props;
    }

    @Bean
    public ProducerFactory<String, ProductDTO> productProducerFactory() {
        return new DefaultKafkaProducerFactory<>(consumerProps());
    }

    @Bean
    public KafkaTemplate<String, ProductDTO> productKafkaTemplate() {
        return new KafkaTemplate<>(productProducerFactory());
    }

    @Bean
    public ProducerFactory<String, ProductDTO[]> productsProducerFactory() {
        return new DefaultKafkaProducerFactory<>(consumerProps());
    }

    @Bean
    public KafkaTemplate<String, ProductDTO[]> productsKafkaTemplate() {
        return new KafkaTemplate<>(productsProducerFactory());
    }

    @Bean
    public Deserializer<String> stringKeyDeserializer() {
        return new StringDeserializer();
    }

    @Bean
    public Deserializer<ProductDTO> gatewayCallBackMessageJsonValueDeserializer() {
        return new JsonDeserializer<>(ProductDTO.class);
    }

    @Bean
    public ConsumerFactory<String, ProductDTO> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerProps(),
                stringKeyDeserializer(), gatewayCallBackMessageJsonValueDeserializer());
    }


    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ProductDTO> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, ProductDTO> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConcurrency(1);
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }

    @Bean
    public Deserializer<ProductDTO[]> gatewayProductsCallBackMessageJsonValueDeserializer() {
        return new JsonDeserializer<>(ProductDTO[].class);
    }

    @Bean
    public ConsumerFactory<String, ProductDTO[]> consumerProductsFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerProps(),
                stringKeyDeserializer(), gatewayProductsCallBackMessageJsonValueDeserializer());
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ProductDTO[]> kafkaProductListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, ProductDTO[]> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConcurrency(1);
        factory.setConsumerFactory(consumerProductsFactory());
        return factory;
    }
}
