package com.ns.task.managementcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@EnableConfigServer
@SpringBootApplication
public class ManagementCloudApplication {

    public static void main(String[] args) {
        SpringApplication.run(ManagementCloudApplication.class, args);
    }
}
