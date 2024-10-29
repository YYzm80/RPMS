package com.example.entity.dto.common;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

@Data
@AllArgsConstructor
@TableName(value = "company")
public class Company implements Serializable {

    /**
     * 企业id
     */
    @TableId(type = IdType.AUTO)
    private Integer cid;
    /**
     * 企业名称
     */
    @Length(max = 255, message = "编码长度不能超过255")
    private String name;
    /**
     * 企业简介
     */
    @Length(max = 255, message = "编码长度不能超过255")
    private String introduce;
    /**
     * 公司图片
     */
    @Length(max = 255, message = "编码长度不能超过255")
    private String photo;
    /**
     * 公司地址
     */
    @Length(max = 255, message = "编码长度不能超过255")
    private String address;

}