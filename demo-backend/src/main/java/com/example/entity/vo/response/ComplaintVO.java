package com.example.entity.vo.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class ComplaintVO {
    private Long cid;
    private Long userId;
    private Long typeId;
    private String submitterName;    // 投诉人
    private String fullAddress;     // 业主房产楼栋地址
    private String content;
    private String statusDesc;      // "待定"/"受理中"/"已处理"
    private String type;
    private Long handlerId;
    private String handlerName;     // 处理人
    private String handleResult;    // 处理结果

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date submitTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date handleTime;
}
