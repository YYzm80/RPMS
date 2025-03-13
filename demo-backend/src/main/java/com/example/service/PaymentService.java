package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.dto.common.Payment;
import com.example.entity.vo.response.PaymentVO;

import java.util.List;

public interface PaymentService extends IService<Payment> {
    List<PaymentVO> getPaymentList();
    List<PaymentVO> getPaymentListByUserId(Long userId);
    PaymentVO getPaymentById(Long id);
    String createPayment(Payment payment, List<Long> userIds);
    String updatePayment(Payment payment);
    String deletePayment(Long id);
}
