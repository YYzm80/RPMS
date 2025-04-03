package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.dto.common.Repair;
import com.example.entity.vo.response.RepairVO;
import com.example.mapper.AccountMapper;
import com.example.mapper.RepairMapper;
import com.example.service.RepairService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RepairServiceImpl extends ServiceImpl<RepairMapper, Repair> implements RepairService {

    @Resource
    private RepairMapper mapper;

    @Resource
    private AccountMapper accountMapper;

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
    public String updateRepair(Repair Repair) {
        return mapper.updateById(Repair) > 0 ? null : "更新报修信息失败，请稍后再试";
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
            if (repair.getHandlerId() != null) v.setHandlerName(accountMapper.selectById(repair.getHandlerId()).getRealName());
        });
    }
}
