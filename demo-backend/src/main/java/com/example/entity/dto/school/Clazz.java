package com.example.entity.dto.school;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("class")
@AllArgsConstructor
public class Clazz implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer cid;
    private String name;
    private String state;

}
