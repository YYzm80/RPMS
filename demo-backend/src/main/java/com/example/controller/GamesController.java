package com.example.controller;

import com.example.entity.RestBean;
import com.example.entity.dto.game.Games;
import com.example.entity.vo.response.GamesVO;
import com.example.service.GamesService;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/games")
public class GamesController {
    @Resource
    private GamesService service;

    @GetMapping("/all")
    public RestBean<List<GamesVO>> all() {
        return RestBean.success(service.getAllGames());
    }

    @GetMapping("/gid")
    public RestBean<GamesVO> gid(Integer gid) {
        return RestBean.success(service.getGamesByGid(gid));
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('admin')")
    public RestBean<String> add(Games vo) {
        String s = service.addGames(vo);
        return s == null ? RestBean.success("新增比赛信息成功") : RestBean.failure(400, s);
    }

    @PostMapping("/update")
    @PreAuthorize("hasRole('admin')")
    public RestBean<String> update(Games vo) {
        String s = service.updateGames(vo);
        return s == null ? RestBean.success("更新比赛信息成功") : RestBean.failure(400, s);
    }

    @PostMapping("/delete")
    @PreAuthorize("hasRole('admin')")
    public RestBean<String> delete(Integer gid) {
        String s = service.deleteGamesByGid(gid);
        return s == null ? RestBean.success("删除比赛信息成功") : RestBean.failure(400, s);
    }
}
