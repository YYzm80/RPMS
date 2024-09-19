package com.example.entity.dto.game;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.entity.BaseData;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("arrangement")
@AllArgsConstructor
public class Arrangement implements Serializable, BaseData {

    @TableId(type = IdType.AUTO)
    private Integer aid;
    private Integer gid;
    private Integer uid;
    private String name;
    private String role;

    public Arrangement() {

    }
}
