package com.example.mapper;

import com.example.config.MyBaseMapper;
import com.example.entity.dto.common.Tags;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TagsMapper extends MyBaseMapper<Tags> {
}
