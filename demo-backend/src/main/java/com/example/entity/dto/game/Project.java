package com.example.entity.dto.game;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("project")
@AllArgsConstructor
public class Project implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer pid;
    private String name;
    @TableField("playerNum")
    private Integer playerNum;
    @TableField("refereeNum")
    private Integer refereeNum;
    private String state;

}
