package com.example.entity.vo.response;

import lombok.Data;

import java.util.Date;

@Data
public class AuthorizeVO {

    private Long userId;
    private String username;
    private String roleName;  // "系统管理员"/"物业人员"/"业主"
    private boolean isOnline;
    private String token;
    private Date expire;
}
