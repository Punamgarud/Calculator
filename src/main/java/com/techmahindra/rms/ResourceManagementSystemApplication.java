package com.techmahindra.rms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ResourceManagementSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(ResourceManagementSystemApplication.class, args);
    }
}