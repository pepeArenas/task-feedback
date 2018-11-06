package com.ns.task.config.properties.kafka;

import com.ns.task.config.properties.BrokerProperties;

//@ConfigurationProperties(prefix = "configuration.broker")
public class KafkaProperties implements BrokerProperties {
    private String exchangesInsertion;
    private String routingKeyInsertion;
    private String queueInsertion;
    private String exchangesRetrieve;
    private String routingKeyRetrieve;
    private String queueRetrieve;
    private String hostname;
    private int port;
    private String username;
    private String password;

    public String getExchangesInsertion() {
        return exchangesInsertion;
    }

    public void setExchangesInsertion(String exchangesInsertion) {
        this.exchangesInsertion = exchangesInsertion;
    }

    public String getRoutingKeyInsertion() {
        return routingKeyInsertion;
    }

    public void setRoutingKeyInsertion(String routingKeyInsertion) {
        this.routingKeyInsertion = routingKeyInsertion;
    }


    public String getExchangesRetrieve() {
        return exchangesRetrieve;
    }

    public void setExchangesRetrieve(String exchangesRetrieve) {
        this.exchangesRetrieve = exchangesRetrieve;
    }

    public String getRoutingKeyRetrieve() {
        return routingKeyRetrieve;
    }

    public void setRoutingKeyRetrieve(String routingKeyRetrieve) {
        this.routingKeyRetrieve = routingKeyRetrieve;
    }

    public String getQueueInsertion() {
        return queueInsertion;
    }

    public void setQueueInsertion(String queueInsertion) {
        this.queueInsertion = queueInsertion;
    }

    public String getQueueRetrieve() {
        return queueRetrieve;
    }

    public void setQueueRetrieve(String queueRetrieve) {
        this.queueRetrieve = queueRetrieve;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
