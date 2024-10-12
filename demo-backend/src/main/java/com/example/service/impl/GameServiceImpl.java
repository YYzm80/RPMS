package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.dto.game.Arrangement;
import com.example.entity.dto.game.Games;
import com.example.entity.dto.game.Project;
import com.example.entity.dto.game.Registration;
import com.example.entity.dto.score.ScoreDetail;
import com.example.entity.dto.score.ScoreList;
import com.example.entity.vo.response.GamesVO;
import com.example.mapper.*;
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

    @Resource
    private ArrangementMapper arrMapper;

    @Resource
    private ScoreMapper scoreMapper;

    @Resource
    private ScoreDetailMapper detailMapper;

    @Resource
    private RegistrationMapper registerMapper;

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
        registerMapper.delete(new QueryWrapper<Registration>().eq("gid", gid));
        List<Arrangement> arrList = arrMapper.selectList(new QueryWrapper<Arrangement>().eq("gid", gid));
        String rs = null;
        if (!arrList.isEmpty()) {
            rs = arrMapper.delete(new QueryWrapper<Arrangement>().eq("gid", gid)) > 0 ? null : "删除比赛安排信息失败，请稍后再试";
            ScoreList scoreList = scoreMapper.selectOne(new QueryWrapper<ScoreList>().eq("gid", gid));
            if (scoreList != null) {
                Integer sid = scoreList.getSid();
                detailMapper.delete(new QueryWrapper<ScoreDetail>().eq("sid", sid));
                rs = scoreMapper.delete(new QueryWrapper<ScoreList>().eq("gid", gid)) > 0 ? null : "删除比赛成绩信息失败，请稍后再试";
            }
        }
        if (rs == null)
            return mapper.deleteById(gid) > 0 ? null : "删除比赛信息失败，请稍后再试";
        return rs;
    }
}
