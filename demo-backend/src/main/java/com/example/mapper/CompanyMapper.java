package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.dto.common.Company;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CompanyMapper extends BaseMapper<Company> {
}
