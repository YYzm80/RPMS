package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.dto.common.Complaint;
import com.example.entity.dto.common.Property;
import com.example.entity.vo.response.ComplaintVO;
import com.example.mapper.AccountMapper;
import com.example.mapper.ComplaintMapper;
import com.example.mapper.PropertyMapper;
import com.example.service.ComplaintService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ComplaintServiceImpl extends ServiceImpl<ComplaintMapper, Complaint> implements ComplaintService {

    @Resource
    private ComplaintMapper mapper;
    @Resource
    private AccountMapper accountMapper;
    @Resource
    private PropertyMapper propertyMapper;

    @Override
    public List<ComplaintVO> getComplaintList() {
        List<Complaint> list = mapper.selectList(null);
        return list.stream()
                .map(this::convert)
                .toList();
    }

    @Override
    public List<ComplaintVO> getComplaintListByUserId(Long userId) {
        List<Complaint> list = mapper.selectList(null);
        return list.stream()
                .filter(complaint -> complaint.getUserId().equals(userId))
                .map(this::convert)
                .toList();
    }

    @Override
    public ComplaintVO getComplaintById(Long id) {
        return convert(mapper.selectById(id));
    }

    /**
     * 申请投诉
     * @param complaint 投诉对象表单
     * @return String
     */
    @Override
    public String addComplaint(Complaint complaint) {
        return mapper.insert(complaint) > 0 ? null : "投诉申请失败，请稍后再试";
    }

    @Override
    public String updateComplaint(Complaint complaint) {
        return mapper.updateById(complaint) > 0 ? null : "更新投诉状态失败，请稍后再试";
    }

    @Override
    public String deleteComplaint(Long id) {
        return mapper.deleteById(id) > 0 ? null : "取消投诉失败，请稍后再试";
    }

    private ComplaintVO convert(Complaint complaint) {
        return complaint.asViewObject(ComplaintVO.class, v -> {
            v.setSubmitterName(accountMapper.selectById(complaint.getUserId()).getRealName());
            String status = complaint.getStatus();
            v.setStatusDesc(status.equals("pending") ? "待定" : status.equals("processing") ? "受理中" : "已解决");
            Property property = propertyMapper.selectOne(new QueryWrapper<Property>().eq("user_id", complaint.getUserId()));
            if (property != null) v.setFullAddress(property.getBuildingNumber() + "栋" + property.getRoomNumber() + "室");
            if (complaint.getHandlerId() != null) v.setHandlerName(accountMapper.selectById(complaint.getHandlerId()).getRealName());
        });
    }
}
