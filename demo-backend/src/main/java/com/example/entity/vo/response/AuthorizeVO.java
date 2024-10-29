package com.example.entity.vo.response;

import lombok.Data;

import java.util.Date;

@Data
public class AuthorizeVO {

    private Integer uid;
    private Integer cid;
    private String name;
    private String username;
    private String role;
    private String avatar;
    private String email;
    private String token;
    private Date expire;
}
