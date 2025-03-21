package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@SpringBootApplication
@EnableMethodSecurity(jsr250Enabled = true)
public class DemoBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoBackendApplication.class, args);
    }

}
