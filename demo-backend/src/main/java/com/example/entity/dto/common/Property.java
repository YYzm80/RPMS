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
@TableName("property")
@AllArgsConstructor
public class Property implements Serializable, BaseData {
    @TableId(type = IdType.AUTO)
    private Long propertyId;
    private Long userId;      // 业主ID
    private Long typeId;
    @Xss
    @NotBlank(message = "楼号不能为空")
    private String buildingNumber;  // 楼号
    @Xss
    @NotBlank(message = "房号不能为空")
    private String roomNumber;  // 房号
    private Double floorArea;  // 面积
    private String status;
    private String floorPlan;   // 户型图

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date purchaseDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdAt;

    public Property() {
    }
}
