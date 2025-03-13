package com.example.mapper;

import com.example.common.config.MyBaseMapper;
import com.example.entity.dto.common.Complaint;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ComplaintMapper extends MyBaseMapper<Complaint> {
}
