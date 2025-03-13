package com.example.controller;

import com.example.entity.RestBean;
import com.example.entity.dto.common.Complaint;
import com.example.entity.vo.response.ComplaintVO;
import com.example.service.ComplaintService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/complaint")
public class ComplaintController {

    @Resource
    private ComplaintService service;

    @GetMapping("/all")
    public RestBean<List<ComplaintVO>> all() {
        return RestBean.success(service.getComplaintList());
    }

    @GetMapping("/uid/{uid}")
    public RestBean<List<ComplaintVO>> getByUid(@PathVariable("uid") Long uid) {
        return RestBean.success(service.getComplaintListByUserId(uid));
    }

    @GetMapping("/cid/{cid}")
    public RestBean<ComplaintVO> get(@PathVariable("cid") Long cid) {
        return RestBean.success(service.getComplaintById(cid));
    }

    @PostMapping("/add")
    public RestBean<String> add(@RequestBody Complaint complaint) {
        String s = service.addComplaint(complaint);
        return s == null ? RestBean.success("投诉申请成功") : RestBean.failure(400, s);
    }

    @PostMapping("/update")
    public RestBean<String> update(@RequestBody Complaint complaint) {
        String s = service.updateComplaint(complaint);
        return s == null ? RestBean.success("投诉状态更新成功") : RestBean.failure(400, s);
    }

    @PostMapping("/delete")
    public RestBean<String> delete(@RequestBody Long cid) {
        String s = service.deleteComplaint(cid);
        return s == null ? RestBean.success("投诉取消成功") : RestBean.failure(400, s);
    }
}
