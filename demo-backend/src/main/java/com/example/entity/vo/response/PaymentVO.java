package com.example.entity.vo.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class PaymentVO {
    private Long payId;
    private Long userId;
    private Long propertyId;
    private String formattedAmount; // "¥ 1,200.00"
    private String username;
    private String fullAddress;
    private String type;          // "物业费"/"停车费"
    private String statusDesc;        // "未支付"/"已支付"
    private Long operatorId;
    private String operatorName;      // 操作人id

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date generateTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date paymentTime;
}
