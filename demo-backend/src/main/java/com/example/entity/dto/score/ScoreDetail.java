package com.example.entity.dto.score;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("score_detail")
@AllArgsConstructor
public class ScoreDetail implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer sid;
    private Integer uid;
    @TableField("gName")
    private String gName;
    private String name;
    private Double score;

}
