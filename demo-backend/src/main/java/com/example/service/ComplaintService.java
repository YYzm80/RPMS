package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.dto.common.Complaint;
import com.example.entity.vo.response.ComplaintVO;

import java.util.List;

public interface ComplaintService extends IService<Complaint> {
    List<ComplaintVO> getComplaintList();
    List<ComplaintVO> getComplaintListByUserId(Long userId);
    ComplaintVO getComplaintById(Long id);
    String addComplaint(Complaint complaint);
    String updateComplaint(Complaint complaint);
    String deleteComplaint(Long id);
}
