package com.example.controller;

import com.example.entity.RestBean;
import com.example.entity.vo.request.location.LocationData;
import com.example.service.LocationDataService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/location")
public class LocationController {
    @Resource
    LocationDataService service;

    @Operation(summary = "获取当前位置信息")
    @GetMapping("/get-location")
    public RestBean<LocationData> getLocation() {
        return RestBean.success(service.getDataByIP());
    }
}
