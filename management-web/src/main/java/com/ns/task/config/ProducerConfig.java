package com.ns.task.config;


import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProducerConfig {


    public static final String EXCHANGE = "x.management";
    public static final String ROUTING_KEY = "management";
    public static final String QUEUE = "q.management.insert";

    public static final String EXCHANGE_GET = "x.management.get";
    public static final String ROUTING_KEY_GET = "management.get";
    public static final String QUEUE_GET = "q.management.get";

    public static final String HOSTNAME = "localhost";
    public static final String USERNAME = "admin";
    public static final String PASSWORD = "admin";
    public static final int PORT = 5672;

    @Bean
    Exchange getExchange() {
        return ExchangeBuilder.directExchange(EXCHANGE).
                build();
    }

    @Bean
    Binding getBinding() {
        return BindingBuilder.bind(getQueue()).
                to(getExchange()).
                with(ROUTING_KEY).
                noargs();
    }

    @Bean
    Queue getQueue() {
        return QueueBuilder.durable(QUEUE).
                autoDelete().
                build();
    }


    @Bean
    Exchange getExchangeForGet() {
        return ExchangeBuilder.directExchange(EXCHANGE_GET).
                build();
    }

    @Bean
    Binding getBindingForGet() {
        return BindingBuilder.bind(getQueueForGet()).
                to(getExchangeForGet()).
                with(ROUTING_KEY_GET).
                noargs();
    }

    @Bean
    Queue getQueueForGet() {
        return QueueBuilder.durable(QUEUE_GET).
                autoDelete().
                build();
    }

    @Bean
    ConnectionFactory connectionFactory() {
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory(HOSTNAME, PORT);
        cachingConnectionFactory.setUsername(USERNAME);
        cachingConnectionFactory.setPassword(PASSWORD);
        return cachingConnectionFactory;
    }

}
