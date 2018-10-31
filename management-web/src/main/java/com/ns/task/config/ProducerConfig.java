package com.ns.task.config;


import com.ns.task.config.properties.RabbitConfig;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProducerConfig {
    @Bean
    ConnectionFactory connectionFactory() {
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory(RabbitConfig.HOSTNAME, RabbitConfig.PORT);
        cachingConnectionFactory.setUsername(RabbitConfig.USERNAME);
        cachingConnectionFactory.setPassword(RabbitConfig.PASSWORD);
        return cachingConnectionFactory;
    }
}
