package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.dto.common.Report;
import com.example.entity.vo.response.MonthlyStatVO;

import java.util.Date;
import java.util.List;

public interface ReportService extends IService<Report> {
    String generateReport(Date month);
    List<MonthlyStatVO> getReportList(Date month);
    MonthlyStatVO getReport(Long id);
    String deleteReport(Long id);
}
