package com.example.entity.vo.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class RepairVO {
    private Long repairId;
    private Long userId;
    private Long typeId;
    private String submitterName;  // 提交人姓名
    private String fullAddress;
    private String description;
    private String handleResult;
    private String type;
    private Long handlerId;
    private String handlerName;   // 处理人姓名
    private String statusDesc;   // "待定"/"处理中"/"已解决"

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date submitTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date completionTime;
}
