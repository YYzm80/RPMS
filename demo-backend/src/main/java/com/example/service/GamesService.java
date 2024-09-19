package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.dto.game.Games;
import com.example.entity.vo.response.GamesVO;

import java.util.List;

public interface GamesService extends IService<Games> {

    List<GamesVO> getAllGames();
    GamesVO getGamesByGid(Integer gid);
    String addGames(Games games);
    String updateGames(Games games);
    String deleteGamesByGid(Integer gid);
}
