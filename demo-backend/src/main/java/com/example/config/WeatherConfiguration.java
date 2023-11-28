package com.example.config;

import jakarta.annotation.Resource;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;

@Configuration
public class WeatherConfiguration {
    @Resource
    private RestTemplateBuilder builder;

    @Bean
    public RestTemplate restTemplate(){
        return  builder.build();
    }
}
