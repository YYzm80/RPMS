package com.example.entity.dto.common;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.entity.BaseData;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("announcement")
@AllArgsConstructor
public class Announcement implements Serializable, BaseData {
    @TableId(type = IdType.AUTO)
    private Long aid;
    private String title;
    private String content;
    private Long publisherId;     // 发布人ID
    private String status;  // "published" 已发布,"deleted" 已删除

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date publishTime;

    public Announcement() {
    }
}
