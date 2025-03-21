package com.example.controller;

import com.example.entity.RestBean;
import com.example.entity.dto.common.Complaint;
import com.example.entity.vo.response.ComplaintVO;
import com.example.service.ComplaintService;
import com.example.util.consts.Const;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/complaint")
public class ComplaintController {

    @Resource
    private ComplaintService service;

    @Operation(summary = "获取投诉列表")
    @GetMapping("/all")
    public RestBean<List<ComplaintVO>> all() {
        return RestBean.success(service.getComplaintList());
    }

    @Operation(summary = "获取用户投诉列表")
    @GetMapping("/uid/{uid}")
    public RestBean<List<ComplaintVO>> getByUid(@PathVariable("uid") Long uid) {
        return RestBean.success(service.getComplaintListByUserId(uid));
    }

    @Operation(summary = "获取投诉详情")
    @GetMapping("/cid/{cid}")
    public RestBean<ComplaintVO> get(@PathVariable("cid") Long cid) {
        return RestBean.success(service.getComplaintById(cid));
    }

    @Operation(summary = "申请投诉")
    @RolesAllowed({Const.ROLE_ADMIN, Const.ROLE_OWNER})
    @PostMapping("/add")
    public RestBean<String> add(@RequestBody @Valid Complaint complaint) {
        String s = service.addComplaint(complaint);
        return s == null ? RestBean.success("投诉申请成功") : RestBean.failure(400, s);
    }

    @Operation(summary = "更新投诉状态")
    @RolesAllowed({Const.ROLE_ADMIN, Const.ROLE_MANAGER, Const.ROLE_OWNER})
    @PutMapping("/update")
    public RestBean<String> update(@RequestBody Complaint complaint) {
        String s = service.updateComplaint(complaint);
        return s == null ? RestBean.success("投诉状态更新成功") : RestBean.failure(400, s);
    }

    @Operation(summary = "取消投诉")
    @RolesAllowed({Const.ROLE_ADMIN, Const.ROLE_OWNER})
    @PostMapping("/delete")
    public RestBean<String> delete(@RequestBody Long cid) {
        String s = service.deleteComplaint(cid);
        return s == null ? RestBean.success("投诉取消成功") : RestBean.failure(400, s);
    }
}
