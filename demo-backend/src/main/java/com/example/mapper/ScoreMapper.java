package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.dto.score.ScoreList;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ScoreMapper extends BaseMapper<ScoreList> {
}
