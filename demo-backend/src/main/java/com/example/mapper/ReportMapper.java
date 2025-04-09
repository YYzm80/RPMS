package com.example.mapper;

import com.example.entity.dto.common.Report;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ReportMapper extends MyBaseMapper<Report>{

    @Insert("INSERT INTO report (month, content, generate_time) VALUES (#{month}, #{content, typeHandler=com.example.common.handler" +
            ".MonthlyStatTypeHandler}, #{generateTime})")
    int insert(Report report);

}
