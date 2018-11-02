package com.ns.task.config;

import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@EnableConfigurationProperties(DatabaseProperties.class)
public class DatabaseConfig {
    private DatabaseProperties properties;

    public DatabaseConfig(DatabaseProperties properties) {
        this.properties = properties;
    }

    @Bean
    DataSource dataSource() {
        return DataSourceBuilder.
                create().
                url(properties.getUrl()).
                driverClassName(properties.getDriverClassName()).
                username(properties.getUsername()).
                password(properties.getPassword()).
                build();
    }
}
