package com.example.entity.vo.request.payment;

import lombok.Data;

@Data
public class PaymentReq {
    private Long id;
    private String type;
    private String status;
}
