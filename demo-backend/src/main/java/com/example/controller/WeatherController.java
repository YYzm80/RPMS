package com.example.controller;

import com.example.entity.RestBean;
import com.example.entity.vo.request.weather.WeatherResponse;
import com.example.service.WeatherDataService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/weather")
public class WeatherController {
    @Resource
    WeatherDataService service;

    @PostMapping("/cityId")
    public RestBean<WeatherResponse> getWeatherByCityId(@RequestParam("city_id") String cityId) {
        return RestBean.success(service.getDataByCityId(cityId));
    }

}
