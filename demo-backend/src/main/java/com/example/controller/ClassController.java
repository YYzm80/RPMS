package com.example.controller;

import com.example.entity.RestBean;
import com.example.entity.dto.school.Clazz;
import com.example.service.ClassService;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/class")
public class ClassController {

    @Resource
    private ClassService service;

    @GetMapping("/all")
    public RestBean<List<Clazz>> all() {
        return RestBean.success(service.getAllClass());
    }

    @GetMapping("/all-state")
    public RestBean<List<Clazz>> state() {
        return RestBean.success(service.getAllClassByState());
    }

    @GetMapping("/cid")
    public RestBean<Clazz> cid(Integer cid) {
        return RestBean.success(service.getClassByCid(cid));
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('admin')")
    public RestBean<String> add(Clazz clazz) {
        String s = service.addClass(clazz);
        return s == null ? RestBean.success("新增班级成功") : RestBean.failure(400, s);
    }

    @PostMapping("/update")
    @PreAuthorize("hasRole('admin')")
    public RestBean<String> update(Clazz clazz) {
        String s = service.updateClass(clazz);
        return s == null ? RestBean.success("修改班级成功") : RestBean.failure(400, s);
    }

    @PostMapping("/delete")
    @PreAuthorize("hasRole('admin')")
    public RestBean<String> delete(Integer cid) {
        String s = service.deleteClass(cid);
        return s == null ? RestBean.success("删除班级成功") : RestBean.failure(400, s);
    }
}
