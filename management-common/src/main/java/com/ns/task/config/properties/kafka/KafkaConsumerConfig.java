package com.ns.task.config.properties.kafka;


import com.ns.task.model.ProductDTO;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
@Profile("kafka")
public class KafkaConsumerConfig {
    @Bean
    public Map<String, Object> consumerProps() {
        Map<String, Object> props = new HashMap<>();
        String groupId = "group_insert";
        String bootstrapServer = "localhost:9092";
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);

        return props;
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
}
