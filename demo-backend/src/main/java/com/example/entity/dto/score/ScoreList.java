package com.example.entity.dto.score;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.entity.BaseData;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("score_list")
@AllArgsConstructor
public class ScoreList implements Serializable, BaseData {

    @TableId(type = IdType.AUTO)
    private Integer sid;
    private Integer gid;
    private String info;
    private String state;


    public ScoreList() {

    }
}
