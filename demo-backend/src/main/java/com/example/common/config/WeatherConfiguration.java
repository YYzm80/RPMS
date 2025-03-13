package com.example.common.config;

import jakarta.annotation.Resource;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class WeatherConfiguration {
    @Resource
    private RestTemplateBuilder builder;

    @Bean
    public RestTemplate restTemplate(){
        return  builder.build();
    }

}
