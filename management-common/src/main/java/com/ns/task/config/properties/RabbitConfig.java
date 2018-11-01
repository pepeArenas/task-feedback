package com.ns.task.config.properties;


import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;

@Configuration
public class RabbitConfig implements RabbitListenerConfigurer {
    @Autowired
    private RabbitProperties properties;

    public static final String EXCHANGE = "x.management";
    public static final String ROUTING_KEY = "management";
    public static final String EXCHANGE_GET = "x.management.get";
    public static final String ROUTING_KEY_GET = "management.get";

    @Bean
    Exchange getExchange() {
        return ExchangeBuilder.directExchange(properties.getExchangesInsertion()).
                build();
    }

    @Bean
    Binding getBinding() {
        return BindingBuilder.bind(getQueue()).
                to(getExchange()).
                with(properties.getRoutingKeyInsertion()).
                noargs();
    }

    @Bean
    Queue getQueue() {
        return QueueBuilder.durable(properties.getQueueInsertion()).
                autoDelete().
                build();
    }

    @Bean
    Exchange getExchangeForGet() {
        return ExchangeBuilder.directExchange(properties.getExchangesRetrieve()).
                build();
    }

    @Bean
    Binding getBindingForGet() {
        return BindingBuilder.bind(getQueueForGet()).
                to(getExchangeForGet()).
                with(properties.getRoutingKeyRetrieve()).
                noargs();
    }

    @Bean
    Queue getQueueForGet() {
        return QueueBuilder.durable(properties.getQueueRetrieve()).
                autoDelete().
                build();
    }

    @Bean
    ConnectionFactory connectionFactory() {
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory(
                properties.getHostname(),
                properties.getPort());
        cachingConnectionFactory.setUsername(properties.getUsername());
        cachingConnectionFactory.setPassword(properties.getPassword());
        return cachingConnectionFactory;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(producerJackson2MessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public Jackson2JsonMessageConverter producerJackson2MessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public MappingJackson2MessageConverter consumerJackson2MessageConverter() {
        return new MappingJackson2MessageConverter();
    }

    @Bean
    public DefaultMessageHandlerMethodFactory messageHandlerMethodFactory() {
        DefaultMessageHandlerMethodFactory factory = new DefaultMessageHandlerMethodFactory();
        factory.setMessageConverter(consumerJackson2MessageConverter());
        return factory;
    }

    @Override
    public void configureRabbitListeners(final RabbitListenerEndpointRegistrar registrar) {
        registrar.setMessageHandlerMethodFactory(messageHandlerMethodFactory());
    }

}
