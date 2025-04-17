package com.example.controller;

import com.example.entity.RestBean;
import com.example.entity.vo.response.MonthlyStatVO;
import com.example.service.ReportService;
import com.example.util.consts.Const;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/report")
public class ReportController {
    @Resource
    private ReportService service;

    @Operation(summary = "获取月报列表")
    @GetMapping("/list")
    @RolesAllowed({Const.ROLE_ADMIN, Const.ROLE_MANAGER})
    public RestBean<List<MonthlyStatVO>> getReportList(@JsonFormat(pattern = "yyyy-MM") Date month) {
        return RestBean.success(service.getReportList(month));
    }

    @Operation(summary = "获取月报")
    @GetMapping("/id/{id}")
    @RolesAllowed({Const.ROLE_ADMIN, Const.ROLE_MANAGER})
    public RestBean<MonthlyStatVO> getReport(@PathVariable("id") Long id) {
        return RestBean.success(service.getReport(id));
    }

    @Operation(summary = "生成月报")
    @PostMapping("/generate")
    @RolesAllowed({Const.ROLE_ADMIN})
    public RestBean<String> generateReport(Date month) {
        String s = service.generateReport(month);
        return s == null ? RestBean.success("生成月报成功") : RestBean.failure(400, s);
    }

    @Operation(summary = "删除月报")
    @PutMapping("/delete")
    @RolesAllowed({Const.ROLE_ADMIN})
    public RestBean<String> deleteReport(Long id) {
        String s = service.deleteReport(id);
        return s == null ? RestBean.success("删除月报成功") : RestBean.failure(400, s);
    }
}
