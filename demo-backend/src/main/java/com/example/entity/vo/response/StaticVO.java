package com.example.entity.vo.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class StaticVO {

    private Integer sid;
    private Integer mid;
    private String meetingName;
    private Integer companyNum;
    private Integer recruitmentNum;
    private Integer studentNum;
    private Integer resumeNum;
    private Integer interviewNum;
    private Double interviewRate;
    private Double interviewPassRate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
}
