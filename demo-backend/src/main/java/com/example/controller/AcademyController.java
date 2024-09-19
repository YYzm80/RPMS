package com.example.controller;


import com.example.entity.RestBean;
import com.example.entity.dto.school.Academy;
import com.example.service.AcademyService;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/academy")
public class AcademyController {

    @Resource
    AcademyService service;

    @GetMapping("/all")
    public RestBean<List<Academy>> all() {
        return RestBean.success(service.getAllAcademy());
    }

    @GetMapping("/state")
    public RestBean<List<Academy>> state() {
        return RestBean.success(service.getAcademyByState());
    }

    @GetMapping("/aid")
    public RestBean<Academy> aid(Integer aid) {
        return RestBean.success(service.getAcademyByAid(aid));
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('admin')")
    public RestBean<String> add(Academy academy) {
        String s = service.addAcademy(academy.getName());
        return s == null ? RestBean.success("添加学院成功") : RestBean.failure(400, s);
    }

    @PostMapping("/update")
    @PreAuthorize("hasRole('admin')")
    public RestBean<String> update(Academy academy) {
        String s = service.updateAcademy(academy);
        return s == null ? RestBean.success("更新学院信息成功") : RestBean.failure(400, s);
    }

    @PostMapping("/delete")
    @PreAuthorize("hasRole('admin')")
    public RestBean<String> delete(Integer aid) {
        String s = service.deleteAcademyByAid(aid);
        return s == null ? RestBean.success("删除学院成功") : RestBean.failure(400, s);
    }
}
