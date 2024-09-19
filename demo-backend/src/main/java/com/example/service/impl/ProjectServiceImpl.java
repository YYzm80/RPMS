package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.dto.game.Games;
import com.example.entity.dto.game.Project;
import com.example.mapper.GamesMapper;
import com.example.mapper.ProjectMapper;
import com.example.service.ProjectService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectServiceImpl extends ServiceImpl<ProjectMapper, Project> implements ProjectService {

    @Resource
    private ProjectMapper mapper;

    @Resource
    private GamesMapper gamesMapper;

    @Override
    public List<Project> getAllProjects() {
        return mapper.selectList(null);
    }

    @Override
    public List<Project> getAllProjectsByState() {
        return mapper.selectList(new QueryWrapper<Project>().eq("state", "1"));
    }

    @Override
    public Project getProjectByPid(Integer pid) {
        return mapper.selectOne(new QueryWrapper<Project>().eq("pid", pid));
    }

    @Override
    public String addProject(Project project) {
        return mapper.insert(project) > 0 ? null : "新增项目失败，请稍后再试";
    }

    @Override
    public String updateProject(Project project) {
        List<Games> games = gamesMapper.selectList(new QueryWrapper<Games>().eq("pid", project.getPid()));
        games.forEach(game -> {
            game.setPlayerNum(project.getPlayerNum());
            game.setRefereeNum(project.getRefereeNum());
            gamesMapper.updateById(game);
        });
        return mapper.updateById(project) > 0 ? null : "更新项目信息失败，请稍后再试";
    }

    @Override
    public String deleteProjectByPid(Integer pid) {
        if (gamesMapper.selectOne(new QueryWrapper<Games>().eq("pid", pid)) != null) {
            return "删除项目失败，请先删除有该项目的所有比赛";
        } else {
            return mapper.delete(new QueryWrapper<Project>().eq("pid", pid)) > 0 ? null : "删除项目失败，请稍后再试";
        }
    }
}
