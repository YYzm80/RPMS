package com.example.controller;

import com.example.entity.RestBean;
import com.example.entity.vo.response.HomeStatic;
import com.example.entity.vo.response.StaticVO;
import com.example.service.StaticService;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/static")
public class StaticController {
    @Resource
    private StaticService service;

    @GetMapping("/all-home")
    public RestBean<HomeStatic> allHome() {
        return RestBean.success(service.getHomeStatic());
    }

    @GetMapping("/static-mid/{mid}")
    public RestBean<StaticVO> staticMid(@PathVariable("mid") Integer mid) {
        return RestBean.success(service.getStaticByMid(mid));
    }

    @PostMapping("/do-static")
    @PreAuthorize("hasAnyRole('admin', 'counsellor')")
    public RestBean<String> doStatic(Integer mid) {
        String s = service.doStatic(mid);
        return s == null ? RestBean.success("统计数据完毕") : RestBean.failure(400, s);
    }

}
