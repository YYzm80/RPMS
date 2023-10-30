package com.example.entity.auth;

import lombok.Data;

@Data
public class Account {
    private int id;
    private String email;
    private String username;
    private String password;
}
