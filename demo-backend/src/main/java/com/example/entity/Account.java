package com.example.entity;

import lombok.Data;

@Data
public class Account {
    private int id;
    private String email;
    private String username;
    private String password;
}
