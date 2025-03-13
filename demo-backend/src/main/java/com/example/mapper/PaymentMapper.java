package com.example.mapper;

import com.example.common.config.MyBaseMapper;
import com.example.entity.dto.common.Payment;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PaymentMapper extends MyBaseMapper<Payment> {
}
