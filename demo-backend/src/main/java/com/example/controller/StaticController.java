package com.example.controller;

import com.example.entity.RestBean;
import com.example.entity.vo.response.HomeDataBackendVO;
import com.example.entity.vo.response.HomeDataVO;
import com.example.service.StaticService;
import com.example.util.consts.Const;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/static")
public class StaticController {

    @Resource
    private StaticService service;

    @Operation(summary = "获取首页数据")
    @GetMapping("/getHomeData")
    public RestBean<HomeDataVO> getHomeData() {
        return RestBean.success(service.getHomeData());
    }

    @Operation(summary = "获取后台首页数据")
    @RolesAllowed({Const.ROLE_ADMIN, Const.ROLE_MANAGER})
    @GetMapping("/getHomeDataBackend/{handlerId}")
    public RestBean<HomeDataBackendVO> getHomeDataBackend(@PathVariable("handlerId") Long handlerId) {
        return RestBean.success(service.getHomeDataBackend(handlerId));
    }
}
