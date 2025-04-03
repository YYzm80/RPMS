package com.example.mapper;

import com.example.entity.dto.common.LogDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LogMapper extends MyBaseMapper<LogDTO> {
}
