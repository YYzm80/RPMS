package com.example.controller;

import com.example.entity.RestBean;
import com.example.entity.vo.request.weather.WeatherResponse;
import com.example.service.WeatherDataService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/weather")
public class WeatherController {
    @Resource
    WeatherDataService service;

    @Operation(summary = "根据城市ID获取天气信息")
    @GetMapping("/cityId/{city_id}")
    public RestBean<WeatherResponse> getWeatherByCityId(@PathVariable("city_id") String cityId) {
        return RestBean.success(service.getDataByCityId(cityId));
    }

}
