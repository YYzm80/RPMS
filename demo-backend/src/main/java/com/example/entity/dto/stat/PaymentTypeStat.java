package com.example.entity.dto.stat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 收费类型统计实体（用于年度/月度收费分类占比统计）
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentTypeStat {
    /**
     * 收费类型（如：物业费、停车费、水电费）
     */
    private String paymentType;

    /**
     * 该类型总金额（单位：元）
     */
    private BigDecimal totalAmount;

    /**
     * 该类型占比（0-1的小数，可选字段）
     */
    private BigDecimal percentage;
}