package com.example.entity.dto.game;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.entity.BaseData;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("registration")
@AllArgsConstructor
public class Registration implements Serializable, BaseData {

    @TableId(type = IdType.AUTO)
    private Integer rid;
    private Integer uid;
    private Integer gid;
    private String role;
    private String state;

}
