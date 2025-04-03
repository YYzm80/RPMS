package com.example.entity.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class PaymentSession {
    private Long pid;
    private BigDecimal amount;
    private String paymentType;

    public PaymentSession() {}
}
