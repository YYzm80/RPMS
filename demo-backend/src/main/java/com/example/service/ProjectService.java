package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.dto.game.Project;

import java.util.List;

public interface ProjectService extends IService<Project> {
    List<Project> getAllProjects();
    List<Project> getAllProjectsByState();
    Project getProjectByPid(Integer pid);
    String addProject(Project project);
    String updateProject(Project project);
    String deleteProjectByPid(Integer pid);
}
