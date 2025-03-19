package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.dto.common.Payment;
import com.example.entity.dto.common.Property;
import com.example.entity.vo.request.payment.PaymentReq;
import com.example.entity.vo.response.PaymentVO;
import com.example.mapper.AccountMapper;
import com.example.mapper.PaymentMapper;
import com.example.mapper.PropertyMapper;
import com.example.service.PaymentService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

@Service
public class PaymentServiceImpl extends ServiceImpl<PaymentMapper, Payment> implements PaymentService {

    @Resource
    PaymentMapper mapper;

    @Resource
    AccountMapper accountMapper;

    @Resource
    PropertyMapper propertyMapper;

    @Override
    public List<PaymentVO> getPaymentList() {
        List<Payment> list = mapper.selectList(null);
        return list.stream()
                .map(this::convert)
                .toList();
    }

    @Override
    public List<PaymentVO> getPaymentListByReq(PaymentReq req) {
        // 必要参数校验（根据业务需求选择是否抛出异常）
        if (req.getId() == null) {
            throw new IllegalArgumentException("账单ID不能为空");
        }

        // 构建查询条件
        LambdaQueryWrapper<Payment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Payment::getUserId, req.getId());  // 必须条件
        if (StringUtils.hasText(req.getType())) {
            queryWrapper.eq(Payment::getType, req.getType());
        }

        if (StringUtils.hasText(req.getStatus())) {
            queryWrapper.eq(Payment::getStatus, req.getStatus());
        }

        // 执行查询
        List<Payment> list = mapper.selectList(queryWrapper);
        return list.stream()
                .map(this::convert)
                .toList();
    }

    @Override
    public PaymentVO getPaymentById(Long id) {
        return convert(mapper.selectById(id));
    }

    /**
     * 创建账单
     * @param payment 账单表单对象
     * @param userIds 缴费人id列表
     * @return String null表示成功，否则返回错误信息
     */
    @Override
    public String createPayment(Payment payment, List<Long> userIds) {
        List<Payment> payments = userIds.stream()
                .map(userId -> {
                    Payment p = new Payment();
                    p.setUserId(userId);
                    Property property = propertyMapper.selectOne(new QueryWrapper<Property>().eq("user_id", userId));
                    p.setPropertyId(propertyMapper.selectById(property).getPropertyId());
                    p.setAmount(payment.getAmount());
                    p.setType(payment.getType());
                    p.setStatus("unpaid");
                    p.setOperatorId(payment.getOperatorId());
                    p.setGenerateTime(new Date());
                    return p;
                }).toList();
        return mapper.insertBatchSomeColumn(payments) > 0 ? null : "发出账单失败，请稍后再试";
    }

    @Override
    public String updatePayment(Payment payment) {
        return mapper.updateById(payment) > 0 ? null : "缴费失败，请稍后再试";
    }

    @Override
    public String deletePayment(Long id) {
        return mapper.deleteById(id) > 0 ? null : "删除账单失败，请稍后再试";
    }

    private PaymentVO convert(Payment payment) {
        return payment.asViewObject(PaymentVO.class, v -> {
            v.setUsername(accountMapper.selectById(payment.getUserId()).getRealName());
            Property property = propertyMapper.selectById(payment.getPropertyId());
            v.setFullAddress(property.getBuildingNumber() + "栋" + property.getRoomNumber() + "室");
            v.setFormattedAmount(payment.getAmount().toString() + "￥");
            String status = payment.getStatus();
            v.setStatusDesc(status.equals("unpaid") ? "未支付" : "已支付");
            v.setOperatorName(accountMapper.selectById(payment.getOperatorId()).getRealName());
        });
    }
}
