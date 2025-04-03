package com.example.entity.dto.auth;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.common.annotation.Xss;
import com.example.entity.BaseData;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
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
    @Xss
    @NotBlank(message = "用户名不能为空")
    private String username;
    private String password;  // 加密后的密码
    @Xss
    @NotBlank(message = "真实姓名不能为空")
    private String realName;
    @Xss
    private String phone;
    @Xss
    private String address;
    private Long rid;      // "1"/"2"/"3"
    @Xss
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
