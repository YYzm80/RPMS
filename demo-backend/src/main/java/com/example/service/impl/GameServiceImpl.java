package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.dto.game.Games;
import com.example.entity.dto.game.Project;
import com.example.entity.vo.response.GamesVO;
import com.example.mapper.GamesMapper;
import com.example.mapper.ProjectMapper;
import com.example.service.GamesService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GameServiceImpl extends ServiceImpl<GamesMapper, Games> implements GamesService {

    @Resource
    private GamesMapper mapper;

    @Resource
    private ProjectMapper projectMapper;

    @Override
    public List<GamesVO> getAllGames() {
        List<Games> games = mapper.selectList(null);
        List<GamesVO> gamesVOS = new ArrayList<>();
        for (Games game : games) {
            gamesVOS.add(game.asViewObject(GamesVO.class,
                    v -> v.setPName(projectMapper.selectOne(
                            new QueryWrapper<Project>()
                            .eq("pid", game.getPid())).getName())));
        }
        return gamesVOS;
    }

    @Override
    public GamesVO getGamesByGid(Integer gid) {
        Games game = mapper.selectById(gid);
        return game.asViewObject(GamesVO.class,
                v -> v.setPName(projectMapper.selectOne(
                        new QueryWrapper<Project>()
                        .eq("pid", game.getPid())).getName()));
    }

    @Override
    public String addGames(Games games) {
        Integer pid = games.getPid();
        Project project = projectMapper.selectById(pid);
        games.setPlayerNum(project.getPlayerNum());
        games.setRefereeNum(project.getRefereeNum());
        return mapper.insert(games) > 0 ? null : "新增比赛信息失败，请稍后再试";
    }

    @Override
    public String updateGames(Games games) {
        Integer pid = games.getPid();
        Project project = projectMapper.selectById(pid);
        games.setPlayerNum(project.getPlayerNum());
        games.setRefereeNum(project.getRefereeNum());
        return mapper.updateById(games) > 0 ? null : "更新比赛信息失败，请稍后再试";
    }

    @Override
    public String deleteGamesByGid(Integer gid) {
        return mapper.deleteById(gid) > 0 ? null : "删除比赛信息失败，请稍后再试";
    }
}
