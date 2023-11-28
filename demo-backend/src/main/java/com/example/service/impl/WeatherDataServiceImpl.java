package com.example.service.impl;

import com.example.entity.weather.WeatherResponse;
import com.example.service.WeatherDataService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Service
public class WeatherDataServiceImpl implements WeatherDataService {

    @Resource
    private RestTemplate restTemplate;
    private final String WEATHER_API = "https://restapi.amap.com/v3/weather/weatherInfo?key=077e7585e935dd09dcdf2098c801cad4&";

    @Override
    public WeatherResponse getDataByCityId(String cityId) {
        String uri = WEATHER_API + "city=" + cityId;
        System.out.println(uri);
        return this.doGetWeatherData(uri);
    }


    private WeatherResponse doGetWeatherData(String uri) {
        ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
        String strBody = null;
        if (response.getStatusCode().value() == 200) {
            strBody = response.getBody();
        }
        ObjectMapper mapper = new ObjectMapper();
        WeatherResponse weather;
        try {
            weather = mapper.readValue(strBody, WeatherResponse.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return weather;
    }
}
