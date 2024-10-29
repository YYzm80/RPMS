package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.dto.interview.Delivery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DeliveryMapper extends BaseMapper<Delivery> {

    @Select("SELECT d.* FROM delivery AS d LEFT JOIN recruitment_information AS r ON d.rid = r.rid WHERE cid = #{cid}")
    List<Delivery> selectDeliveryWithRecruitmentInfoByCid(Integer cid);
}
