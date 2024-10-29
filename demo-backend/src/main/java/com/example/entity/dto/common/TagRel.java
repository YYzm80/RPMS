package com.example.entity.dto.common;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
@TableName("tags_rel")
public class TagRel implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer rid;
    private Integer tid;

    public TagRel() {

    }
}
