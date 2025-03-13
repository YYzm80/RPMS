package com.example.controller;

import com.example.entity.RestBean;
import com.example.entity.vo.response.HomeDataVO;
import com.example.service.StaticService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/static")
public class StaticController {

    @Resource
    private StaticService service;

    @GetMapping("/getHomeData")
    public RestBean<HomeDataVO> getHomeData() {
        return RestBean.success(service.getHomeData());
    }
}
