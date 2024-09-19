package com.example.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.dto.school.Academy;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
* @author User
* @description 针对表【academy】的数据库操作Mapper
* @createDate 2024-06-19 09:05:13
* @Entity com.backend.entity.lab.Academy
*/
@Mapper
public interface AcademyMapper extends BaseMapper<Academy> {

    @Select("select * from academy")
    List<Academy> getAllAcademy();

    @Select("select * from academy where state = '1'")
    List<Academy> getAllAcademyByState();

    @Select("select * from academy where aid = #{aid}")
    Academy getAcademyByAId(Integer aid);

    @Insert("insert into academy(name, state) VALUES (#{name}, '1')")
    Integer addAcademy(String name);

    @Update("update academy set name = #{name}, state = #{state} where aid = #{aid}")
    Integer updateAcademy(Academy academy);

    @Delete("delete from academy where aid = #{aid}")
    Integer deleteAcademyByAid(Integer aid);
}




