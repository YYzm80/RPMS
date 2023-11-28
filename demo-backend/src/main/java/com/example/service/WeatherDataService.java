package com.example.service;

import com.example.entity.weather.WeatherResponse;

public interface WeatherDataService {
    WeatherResponse getDataByCityId(String cityId);
}
