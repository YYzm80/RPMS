package com.example.entity.dto.common;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@TableName("types")
@AllArgsConstructor
public class Types {
    @TableId(type = IdType.AUTO)
    private Long tid;
    private String type;
    private String description;
    private String status;
}
