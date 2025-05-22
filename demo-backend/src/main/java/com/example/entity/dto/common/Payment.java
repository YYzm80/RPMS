package com.example.entity.dto.common;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.common.annotation.Xss;
import com.example.entity.BaseData;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("payment")
@AllArgsConstructor
public class Payment implements Serializable, BaseData {
    @TableId(type = IdType.AUTO)
    private Long payId;
    private Long userId;
    private Long propertyId;
    private Long typeId;

    private BigDecimal amount;
    @Xss
    private String type;          // "物业费"/"停车费"
    private String status;        // "unpaid"/"paid"
    private Long operatorId;      // 操作人id

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date generateTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date paymentTime;

    public Payment() {
    }
}
