package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.dto.game.Project;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProjectMapper extends BaseMapper<Project> {
}
