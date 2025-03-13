package com.example.entity.vo.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class AnnouncementVO {
    private Long aid;
    private Long publisherId;
    private String publisherName; // 发布人姓名
    private String title;
    private String content;
    private String statusDesc; // "已发布"/"已删除"

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date publishTime;
}
