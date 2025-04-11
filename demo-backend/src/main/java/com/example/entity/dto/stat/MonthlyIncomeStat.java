package com.example.entity.dto.stat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * 月度收入统计
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyIncomeStat {
    /**
     * 收入总计
     */
    BigDecimal total;
    /**
     * 收入类型统计
     */
    List<PaymentTypeStat> paymentTypeStatList;
}
