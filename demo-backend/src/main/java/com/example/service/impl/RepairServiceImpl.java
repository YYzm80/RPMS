package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.dto.common.Payment;
import com.example.entity.dto.common.Property;
import com.example.entity.dto.common.Repair;
import com.example.entity.dto.common.Types;
import com.example.entity.vo.request.repair.RepairReq;
import com.example.entity.vo.response.RepairVO;
import com.example.mapper.*;
import com.example.service.RepairService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class RepairServiceImpl extends ServiceImpl<RepairMapper, Repair> implements RepairService {

    @Resource
    private RepairMapper mapper;
    @Resource
    private AccountMapper accountMapper;
    @Resource
    private PropertyMapper propertyMapper;
    @Resource
    private PaymentMapper paymentMapper;
    @Resource
    private TypeMapper typeMapper;

    @Override
    public List<RepairVO> getRepairList() {
        List<Repair> list = mapper.selectList(null);
        return list.stream()
                .map(this::convert)
                .toList();
    }

    @Override
    public List<RepairVO> getRepairListByUserId(Long userId) {
        List<Repair> list = mapper.selectList(null);
        return list.stream()
                .filter(repair -> repair.getUserId().equals(userId))
                .map(this::convert)
                .toList();
    }

    @Override
    public RepairVO getRepairById(Long id) {
        return convert(mapper.selectById(id));
    }

    /**
     * 报修对象表单添加
     *
     * @param Repair 报修对象表单
     * @return String
     */
    @Override
    public String addRepair(Repair Repair) {
        return mapper.insert(Repair) > 0 ? null : "申请报修失败，请稍后再试";
    }

    @Override
    public String updateRepair(RepairReq req) {
        Repair repair = req.getRepair();
        if (req.getPrice() != null && req.getPrice().compareTo(BigDecimal.ZERO) > 0) {
            Payment payment = new Payment();
            Long userId = mapper.selectById(repair.getRepairId()).getUserId();
            payment.setUserId(userId);
            payment.setPropertyId(propertyMapper.selectOne(new QueryWrapper<Property>()
                    .eq("user_id", userId)).getPropertyId());
            payment.setAmount(req.getPrice());
            payment.setTypeId(typeMapper.selectOne(new QueryWrapper<Types>().eq("type", "报修费")).getTid());
            payment.setType("报修费");
            payment.setStatus("unpaid");
            payment.setOperatorId(repair.getHandlerId());
            payment.setGenerateTime(new Date());
            if (!(paymentMapper.insert(payment) > 0)) return "创建报修账单失败，请稍后再试";
        }
        return mapper.updateById(repair) > 0 ? null : "更新报修信息失败，请稍后再试";
    }

    @Override
    public String deleteRepair(Long id) {
        return mapper.deleteById(id) > 0 ? null : "取消报修失败，请稍后再试";
    }

    private RepairVO convert(Repair repair) {
        return repair.asViewObject(RepairVO.class, v -> {
            v.setSubmitterName(accountMapper.selectById(repair.getUserId()).getRealName());
            String status = repair.getStatus();
            v.setStatusDesc(status.equals("pending") ? "待定" : status.equals("processing") ? "处理中" : "已解决");
            Property property = propertyMapper.selectOne(new QueryWrapper<Property>().eq("user_id", repair.getUserId()));
            if (property != null) v.setFullAddress(property.getBuildingNumber() + "栋" + property.getRoomNumber() + "室");
            if (repair.getHandlerId() != null) v.setHandlerName(accountMapper.selectById(repair.getHandlerId()).getRealName());
        });
    }
}
