package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.dto.common.Resume;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ResumeMapper extends BaseMapper<Resume> {
}
