package com.example.controller;

import com.example.entity.RestBean;
import com.example.entity.dto.game.Project;
import com.example.service.ProjectService;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/project")
public class ProjectController {
    @Resource
    private ProjectService service;

    @GetMapping("/all")
    public RestBean<List<Project>> all() {
        return RestBean.success(service.getAllProjects());
    }

    @GetMapping("/all-state")
    public RestBean<List<Project>> state() {
        return RestBean.success(service.getAllProjectsByState());
    }

    @GetMapping("/pid")
    public RestBean<Project> pid(Integer pid) {
        return RestBean.success(service.getProjectByPid(pid));
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('admin')")
    public RestBean<String> add(Project project) {
        String s = service.addProject(project);
        return s == null ? RestBean.success("新增项目成功") : RestBean.failure(400, s);
    }

    @PostMapping("/update")
    @PreAuthorize("hasRole('admin')")
    public RestBean<String> update(Project project) {
        String s = service.updateProject(project);
        return s == null ? RestBean.success("更新项目信息成功") : RestBean.failure(400, s);
    }

    @PostMapping("/delete")
    @PreAuthorize("hasRole('admin')")
    public RestBean<String> delete(Integer pid) {
        String s = service.deleteProjectByPid(pid);
        return s == null ? RestBean.success("删除项目成功") : RestBean.failure(400, s);
    }

}
