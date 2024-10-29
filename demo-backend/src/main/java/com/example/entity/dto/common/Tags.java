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
@TableName("tags")
public class Tags implements Serializable {

    /**
    * 招聘标签id
    */
    @TableId(type = IdType.AUTO)
    private Integer tid;
    /**
    * 标签名称
    */
    @Length(max= 255,message="编码长度不能超过255")
    private String name;
    /**
    * 启用状态
    */
    private String state;

}
