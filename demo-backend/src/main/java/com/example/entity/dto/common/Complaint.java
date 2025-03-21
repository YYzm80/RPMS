package com.example.entity.dto.common;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.common.annotation.Xss;
import com.example.entity.BaseData;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("complaint")
@AllArgsConstructor
public class Complaint implements Serializable, BaseData {
    @TableId(type = IdType.AUTO)
    private Long cid;
    private Long userId;    // 投诉人id
    @Xss
    @NotBlank(message = "投诉内容不能为空")
    private String content;
    private String status;      // 'pending','processing','resolved'
    private Long handlerId;     // 处理人id
    @Xss
    private String handleResult;    // 处理结果

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date submitTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date handleTime;
}
