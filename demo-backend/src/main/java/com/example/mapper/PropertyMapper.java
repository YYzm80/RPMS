package com.example.mapper;

import com.example.common.config.MyBaseMapper;
import com.example.entity.dto.common.Property;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PropertyMapper extends MyBaseMapper<Property> {
}
