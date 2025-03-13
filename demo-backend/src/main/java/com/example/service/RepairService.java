package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.dto.common.Repair;
import com.example.entity.vo.response.RepairVO;

import java.util.List;

public interface RepairService extends IService<Repair> {
    List<RepairVO> getRepairList();
    List<RepairVO> getRepairListByUserId(Long userId);
    RepairVO getRepairById(Long id);
    String addRepair(Repair Repair);
    String updateRepair(Repair Repair);
    String deleteRepair(Long id);
}
