package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.dto.common.Payment;
import com.example.entity.vo.request.payment.PaymentReq;
import com.example.entity.vo.response.PaymentVO;

import java.util.List;

public interface PaymentService extends IService<Payment> {
    List<PaymentVO> getPaymentList();
    List<PaymentVO> getPaymentListByReq(PaymentReq req);
    PaymentVO getPaymentById(Long id);
    String createPayment(Payment payment, List<Long> userIds);
    String autoCreatePayment(Payment payment, Double singleAmount);
    String updatePayment(Payment payment);
    String deletePayment(Long id);
}
