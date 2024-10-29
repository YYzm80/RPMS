package com.example.entity.vo.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class AccountVO {

    private Integer uid;
    private Integer cid;
    private String companyName;
    private String email;
    private String name;
    private String username;
    private String role;
    private String avatar;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date create_time;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date update_time;
}
