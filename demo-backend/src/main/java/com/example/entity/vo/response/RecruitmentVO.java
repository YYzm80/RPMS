package com.example.entity.vo.response;

import com.example.entity.dto.common.Tags;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class RecruitmentVO {
    private Integer rid;
    private Integer cid;
    private Integer mid;
    private String name;
    private String companyName;
    private String img;
    private String meetingName;
    private String region;
    private Long salary;
    private String description;
    private String state;
    private List<Tags> tagsList;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
}
