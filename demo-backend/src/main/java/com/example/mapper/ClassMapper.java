package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.dto.school.Clazz;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ClassMapper extends BaseMapper<Clazz> {

}
