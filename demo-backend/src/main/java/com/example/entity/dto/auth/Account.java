package com.example.entity.dto.auth;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.entity.BaseData;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("user")
@AllArgsConstructor
public class Account implements Serializable, BaseData {
    @TableId(type = IdType.AUTO)
    private Long userId;
    private String username;
    private String password;  // 加密后的密码
    private String realName;
    private String phone;
    private String address;
    private Long rid;      // "1"/"2"/"3"
    private String position;    // 仅工作人员有效
    private String gender;
    private String status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date hireDate;  // 仅工作人员有效

    public Account() {
    }

}
