package com.example.entity.vo.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class AccountVO {

    private Long userId;
    private String username;
    private String realName;
    private String phone;
    private String roleName;  // "系统管理员"/"物业人员"/"业主"
    private String position;
    private String gender;
    private String address;
    private String status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date hireDate;
}
