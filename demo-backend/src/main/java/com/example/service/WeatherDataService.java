package com.example.service;

import com.example.entity.vo.request.weather.WeatherResponse;

public interface WeatherDataService {
    WeatherResponse getDataByCityId(String cityId);
}
