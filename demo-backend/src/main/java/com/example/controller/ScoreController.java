package com.example.controller;

import com.example.entity.RestBean;
import com.example.entity.dto.score.ScoreDetail;
import com.example.entity.vo.response.ScoreDetailVO;
import com.example.entity.vo.response.ScoreListVO;
import com.example.service.ScoreService;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/score")
public class ScoreController {

    @Resource
    private ScoreService service;

    @GetMapping("/all-detail-sid")
    public RestBean<List<ScoreDetailVO>> allDetailSid(Integer sid) {
        return RestBean.success(service.getAllScoreBySid(sid));
    }

    @GetMapping("/all-detail-sid-sort")
    public RestBean<List<ScoreDetailVO>> allDetailSidSort(Integer sid) {
        return RestBean.success(service.getAllScoreBySidSort(sid));
    }

    @GetMapping("/all-detail-uid")
    public RestBean<List<ScoreDetailVO>> allDetailUid(Integer uid) {
        return RestBean.success(service.getAllScoreByUid(uid));
    }

    @GetMapping("/detail-id")
    public RestBean<ScoreDetailVO> detailId(Integer id) {
        return RestBean.success(service.getScoreById(id));
    }

    @GetMapping("/all-list")
    public RestBean<List<ScoreListVO>> allList() {
        return RestBean.success(service.getAllScoreList());
    }

    @GetMapping("/list-gid")
    public RestBean<ScoreListVO> listGid(Integer gid) {
        return RestBean.success(service.getScoreListByGid(gid));
    }

    @GetMapping("/list-sid")
    public RestBean<ScoreListVO> listSid(Integer sid) {
        return RestBean.success(service.getScoreListBySid(sid));
    }

    @PostMapping("/init")
    @PreAuthorize("hasRole('admin')")
    public RestBean<String> init(Integer gid) {
        String s = service.initScoreList(gid);
        return s == null ? RestBean.success("比赛结束成功，已经初始化成绩") : RestBean.failure(400, s);
    }

    @PostMapping("/publish")
    @PreAuthorize("hasAnyRole('admin', 'teacher')")
    public RestBean<String> publish(Integer sid) {
        String s = service.publishScore(sid);
        return s == null ? RestBean.success("成绩发布成功") : RestBean.failure(400, s);
    }

    @PostMapping("/update-detail")
    @PreAuthorize("hasAnyRole('admin', 'teacher')")
    public RestBean<String> update(ScoreDetail vo) {
        String s = service.updateScore(vo);
        return s == null ? RestBean.success("成绩修改/录入成功") : RestBean.failure(400, s);
    }

    @PostMapping("/delete")
    @PreAuthorize("hasRole('admin')")
    public RestBean<String> delete(Integer id) {
        String s = service.deleteScoreById(id);
        return s == null ? RestBean.success("成绩删除成功") : RestBean.failure(400, s);
    }
}
