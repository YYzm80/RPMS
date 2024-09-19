package com.example.entity.dto.school;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@TableName("academy")
@AllArgsConstructor
public class Academy implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer aid;
    private String name;
    private String state;

    @Serial
    private static final long serialVersionUID = 1L;
}