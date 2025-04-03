package com.example.entity.vo.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class PropertyVO {
    private Long propertyId;
    private Long userId;
    private String fullAddress;    // "A栋101室"
    private String ownerName;      // 关联查询业主姓名
    private String buildingNumber;
    private String roomNumber;
    private Double floorArea;
    private String statusDesc;     // "已入住"/"空置中"
    private String floorPlan;   // 户型图访问URL

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date purchaseDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createAt;
}
