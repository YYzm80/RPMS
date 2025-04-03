package com.example.job;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.entity.dto.common.LogDTO;
import com.example.mapper.LogMapper;
import com.example.util.consts.LogConst;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Component
public class JobTimer{

    @Resource
    private LogMapper mapper;

    /**
     * 定时删除日志
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

}
