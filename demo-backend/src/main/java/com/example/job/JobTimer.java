package com.example.job;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.entity.dto.common.LogDTO;
import com.example.mapper.LogMapper;
import com.example.service.ReportService;
import com.example.util.consts.LogConst;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class JobTimer{

    @Resource
    private LogMapper mapper;
    @Resource
    private ReportService service;

    /**
     * 定时删除日志，每天零点执行一次
     */
    @Scheduled(cron = "0 0 0 ? * ?")
    public void cleanLogs() {
        // 缓存方法名，避免重复调用
        String methodName = "cleanLogs";
        // 定义时间间隔常量，提高可读性
        LocalDate before = LocalDate.now().minusDays(7);

        try {
            log.info(methodName + "定时任务执行开始");
            QueryWrapper<LogDTO> wrapper = new QueryWrapper<>();
            wrapper.lt("created_at", before);
            List<LogDTO> list = mapper.selectList(wrapper);
            if (!list.isEmpty()) {
                mapper.deleteBatchIds(list.stream().map(LogDTO::getId).toList());
            } else {
                log.info(methodName + "没有需要删除的日志");
            }
            log.info(methodName + "定时任务执行结束");
        } catch (Exception e) { // 捕获所有异常
            LogDTO logDTO = new LogDTO();
            logDTO.setType(LogConst.TYPE_ERROR);
            logDTO.setDetail("方法名: " + methodName + ", 异常信息: " + e);
            mapper.insert(logDTO);
            log.error("方法名: " + methodName + ", 异常信息: " + e.getMessage(), e);
        }
    }

    /**
     * 生成月报，每月第一日执行一次
     */
    @Scheduled(cron = "0 0 0 1 * ?")
    public void generateReport() {
        log.info("生成月报定时任务开始");
        SimpleDateFormat std = new SimpleDateFormat("yyyy-MM-dd");

        // 获取上个月日期
        LocalDate lastMonthDate = LocalDate.now().minusMonths(1);
        Date lastMonth;
        try {
            lastMonth = std.parse(lastMonthDate.toString());
            log.info("生成月报: " + lastMonth);
            // 生成月报
            String s = service.generateReport(lastMonth);
            if (s == null) {
                log.info("生成月报成功");
            } else {
                log.info("生成月报失败: " + s);
            }
            log.info("生成月报定时任务结束");
        } catch (ParseException e) {
            log.error("日期格式化错误", e);
        }
    }

}
