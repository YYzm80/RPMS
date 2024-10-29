package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.dto.interview.Interview;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface InterviewMapper extends BaseMapper<Interview> {

    @Select("SELECT i.* FROM interview AS i LEFT JOIN recruitment_information AS r ON i.rid = r.rid WHERE cid = #{cid}")
    List<Interview> selectInterviewWithRecruitmentInfoByCid(Integer cid);
}
