package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.entity.dto.game.Arrangement;
import com.example.entity.dto.score.ScoreDetail;
import com.example.entity.dto.score.ScoreList;
import com.example.entity.vo.response.ScoreDetailVO;
import com.example.entity.vo.response.ScoreListVO;
import com.example.mapper.*;
import com.example.service.ScoreService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ScoreServiceImpl implements ScoreService {

    @Resource
    ScoreMapper mapper;

    @Resource
    ScoreDetailMapper detailMapper;

    @Resource
    AccountMapper accountMapper;

    @Resource
    GamesMapper gamesMapper;

    @Resource
    ArrangementMapper arrMapper;

    @Override
    public List<ScoreDetailVO> getAllScoreBySid(Integer sid) {
        List<ScoreDetail> list = detailMapper.selectList(new QueryWrapper<ScoreDetail>().eq("sid", sid));
        return this.converse(list);
    }

    @Override
    public List<ScoreDetailVO> getAllScoreBySidSort(Integer sid) {
        List<ScoreDetail> list = detailMapper.selectList(new QueryWrapper<ScoreDetail>().eq("sid", sid).orderByDesc("score"));
        return this.converse(list);
    }

    @Override
    public List<ScoreDetailVO> getAllScoreByUid(Integer uid) {
        List<ScoreDetail> list = detailMapper.selectList(new QueryWrapper<ScoreDetail>()
                .select("sid")
                .eq("uid", uid)
                .groupBy("sid"));
        List<ScoreDetail> newList = new ArrayList<>();
        list.forEach( l -> {
            ScoreList scoreList = mapper.selectById(l.getSid());
            if (scoreList.getState().equals("1")) {
                ScoreDetail ScoreDetail = detailMapper.selectOne(new QueryWrapper<ScoreDetail>()
                        .eq("uid", uid).eq("sid", l.getSid()));
                newList.add(ScoreDetail);
            }
        });
        return this.converse(newList);
    }

    @Override
    public List<ScoreListVO> getAllScoreList() {
        List<ScoreList> list = mapper.selectList(null);
        List<ScoreListVO> scoreListVOS = new ArrayList<>();
        list.forEach(l ->
                scoreListVOS.add(l.asViewObject(ScoreListVO.class, v -> v.setName(gamesMapper.selectById(v.getGid()).getName())))
        );
        return scoreListVOS;
    }

    @Override
    public ScoreDetailVO getScoreById(Integer id) {
        ScoreDetail detail = detailMapper.selectById(id);
        return detail.asViewObject(ScoreDetailVO.class, v -> {
            v.setName(accountMapper.selectById(v.getUid()).getName());
            v.setGName(gamesMapper.selectById(mapper.selectById(v.getSid()).getGid()).getName());
        });
    }

    @Override
    public ScoreListVO getScoreListByGid(Integer gid) {
        ScoreList scoreList = mapper.selectOne(new QueryWrapper<ScoreList>().eq("gid", gid));
        if (scoreList != null)
            return scoreList.asViewObject(ScoreListVO.class, v -> v.setName(gamesMapper.selectById(v.getGid()).getName()));
        return null;
    }

    @Override
    public ScoreListVO getScoreListBySid(Integer sid) {
        ScoreList scoreList = mapper.selectById(sid);
        return scoreList.asViewObject(ScoreListVO.class, v -> v.setName(gamesMapper.selectById(v.getGid()).getName()));
    }

    @Override
    public String initScoreList(Integer gid) {
        ScoreList scoreList = new ScoreList();
        scoreList.setGid(gid);
        if (!(mapper.insert(scoreList) > 0)) return "比赛结束失败，成绩列表初始化失败";
        List<Arrangement> arrangements = arrMapper.selectList(new QueryWrapper<Arrangement>()
                .eq("gid", gid)
                .eq("role", "athlete"));
        List<ScoreDetail> scoreDetails = new ArrayList<>();
        arrangements.forEach(a -> {
            ScoreDetail scoreDetail = new ScoreDetail();
            scoreDetail.setSid(mapper.selectOne(new QueryWrapper<ScoreList>().eq("gid", gid)).getSid());
            scoreDetail.setUid(a.getUid());
            scoreDetails.add(scoreDetail);
        });
        return detailMapper.insertBatchSomeColumn(scoreDetails) > 0 ? null : "比赛结束失败，成绩初始化失败";
    }

    @Override
    public String publishScore(Integer sid) {
        ScoreList scoreList = new ScoreList();
        scoreList.setSid(sid);
        scoreList.setState("1");
        return mapper.updateById(scoreList) > 0 ? null : "成绩发布失败，请稍后再试";
    }

    @Override
    public String updateScore(ScoreDetail scoreDetail) {
        return detailMapper.updateById(scoreDetail) > 0 ? null : "成绩修改失败，请稍后再试";
    }

    @Override
    public String deleteScoreById(Integer id) {
        return detailMapper.deleteById(id) > 0 ? null : "成绩删除失败，请稍后再试";
    }

    private List<ScoreDetailVO> converse(List<ScoreDetail> scoreDetails) {
        List<ScoreDetailVO> scoreDetailVOS = new ArrayList<>();
        for (ScoreDetail scoreDetail : scoreDetails) {
            scoreDetailVOS.add(scoreDetail.asViewObject(ScoreDetailVO.class, v -> {
                v.setName(accountMapper.selectById(v.getUid()).getName());
                v.setGName(gamesMapper.selectById(mapper.selectById(v.getSid()).getGid()).getName());
            }));
        }
        return scoreDetailVOS;
    }
}
