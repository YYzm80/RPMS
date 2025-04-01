package com.example;

import com.example.job.JobTimer;
import jakarta.annotation.Resource;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@EnableScheduling
@SpringBootApplication
@EnableMethodSecurity(jsr250Enabled = true)
public class DemoBackendApplication {

    @Resource
    private JobTimer jobTimer;

    public static void main(String[] args) {
        SpringApplication.run(DemoBackendApplication.class, args);
    }

    @Bean
    public ApplicationRunner run() {
        return args -> jobTimer.cleanLogs();
    }

}
