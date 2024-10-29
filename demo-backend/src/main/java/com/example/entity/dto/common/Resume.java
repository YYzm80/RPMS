package com.example.entity.dto.common;

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
@TableName("resume")
public class Resume implements Serializable , BaseData {

    /**
     * 简历id
     */
    @TableId(type = IdType.AUTO)
    private Integer rid;
    /**
     * 简历所属学生id
     */
    private Integer uid;
    /**
     * 简历名称
     */
    @Length(max = 255, message = "编码长度不能超过255")
    private String name;
    /**
     * 简历附件
     */
    @Length(max = 255, message = "编码长度不能超过255")
    private String file;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

}
