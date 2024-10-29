package com.example.entity.vo.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class DeliveryVO {
    private Integer did;
    private Integer uid;
    private Integer rid;
    private Integer resumeId;
    private String username;
    private String resumeFile;
    private String recruitmentName;
    private String state;
    private boolean isInterview;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date deliveryTime;
}
