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
@TableName("repair")
@AllArgsConstructor
public class Repair implements Serializable, BaseData {
    @TableId(type = IdType.AUTO)
    private Long repairId;
    private Long userId;
    private Long typeId;
    @Xss
    @NotBlank(message = "报修描述不能为空")
    private String description;
    private Long handlerId;
    @Xss
    private String handleResult;
    private String status;        // "pending"/"processing"/"completed"

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date submitTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date completionTime;
}
