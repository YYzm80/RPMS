package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.dto.game.Games;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GamesMapper extends BaseMapper<Games> {
}
