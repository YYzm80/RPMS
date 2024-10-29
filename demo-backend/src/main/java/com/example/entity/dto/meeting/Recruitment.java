package com.example.entity.dto.meeting;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.entity.BaseData;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@TableName("recruitment_information")
public class Recruitment implements Serializable, BaseData {
    /**
     * 招聘信息id
     */
    @TableId(type = IdType.AUTO)
    private Integer rid;
    /**
     * 企业id
     */
    private Integer cid;
    /**
     * 参加双选会id
     */
    private Integer mid;
    /**
     * 招聘信息名称
     */
    @Length(max = 255, message = "编码长度不能超过255")
    private String name;
    /**
     * 地区
     */
    @Length(max = 255, message = "编码长度不能超过255")
    private String region;
    /**
     * 薪资
     */
    private Long salary;
    /**
     * 职位描述
     */
    @Length(max = 255, message = "编码长度不能超过255")
    private String description;
    /**
     * 招聘状态
     */
    private String state;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

}
