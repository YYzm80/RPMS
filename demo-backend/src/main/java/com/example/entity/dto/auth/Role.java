package com.example.entity.dto.auth;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("role")
@AllArgsConstructor
public class Role implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long rid;
    private String name;
}
