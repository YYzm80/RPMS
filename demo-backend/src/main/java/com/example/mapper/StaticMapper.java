package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.dto.common.PopularCompany;
import com.example.entity.dto.common.Static;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface StaticMapper extends BaseMapper<Static> {
    @Select("""
            SELECT
            c.NAME AS name,
            COUNT( d.did ) AS deliverNum
            FROM
            company c
            JOIN recruitment_information ri ON c.cid = ri.cid
            JOIN delivery d ON ri.rid = d.rid
            GROUP BY c.cid
            ORDER BY deliverNum DESC
            LIMIT 5
            """)
    List<PopularCompany> getPopularCompany();

    @Select("""
            SELECT COUNT(Distinct uid)
            FROM delivery as d
            LEFT JOIN recruitment_information as r
            ON d.rid = r.rid
            WHERE mid = #{mid}
            """)
    Integer getStudentNum(Integer mid);

    @Select("""
            SELECT COUNT(DISTINCT cid)
            FROM recruitment_information
            WHERE mid = #{mid}
            """)
    Integer getCompanyNum(Integer mid);

    @Select("""
            SELECT COUNT(*)
            FROM delivery as d
            LEFT JOIN recruitment_information as r
            ON d.rid = r.rid
            WHERE mid = #{mid}
            """)
    Integer getResumeNum(Integer mid);

    @Select("""
            SELECT COUNT(*)
            FROM interview as i
            LEFT JOIN recruitment_information as r
            ON i.rid = r.rid
            WHERE mid = #{mid}
            """)
    Integer getInterviewNum(Integer mid);

    @Select("""
            SELECT COUNT(*)
            FROM interview as i
            LEFT JOIN recruitment_information as r
            ON i.rid = r.rid
            WHERE mid = #{mid}
            AND i.state = '1'
            """)
    Integer getInterviewPassNum(Integer mid);
}
