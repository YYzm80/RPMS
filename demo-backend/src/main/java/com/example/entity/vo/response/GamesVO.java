package com.example.entity.vo.response;

import lombok.Data;

import java.util.Date;

@Data
public class GamesVO {

    private Integer gid;
    private Integer pid;
    private String pName;
    private String name;
    private String introduce;
    private Integer playerNum;
    private Integer refereeNum;
    private Date startTime;
}
