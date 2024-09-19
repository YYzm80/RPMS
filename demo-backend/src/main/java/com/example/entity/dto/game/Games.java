package com.example.entity.dto.game;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.entity.BaseData;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("games")
@AllArgsConstructor
public class Games implements Serializable, BaseData {

    @TableId(type = IdType.AUTO)
    private Integer gid;
    private Integer pid;
    private String name;
    private String introduce;
    @TableField("playerNum")
    private Integer playerNum;
    @TableField("refereeNum")
    private Integer refereeNum;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @TableField("start_time")
    private Date startTime;

}
