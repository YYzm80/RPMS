package com.example.job;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.entity.dto.common.LogDTO;
import com.example.mapper.LogMapper;
import com.example.util.consts.LogConst;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

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
        try {
            log.info( this.getClass().getMethod("cleanLogs") + "定时任务执行开始");
            QueryWrapper<LogDTO> wrapper = new QueryWrapper<>();
            wrapper.le("create_at", System.currentTimeMillis() - 60 * 60 * 24 * 7);
            mapper.deleteBatchIds(mapper.selectList(wrapper).stream().map(LogDTO::getId).toList());
            log.info( this.getClass().getMethod("cleanLogs") + "定时任务执行结束");
        } catch (NoSuchMethodException e) {
            LogDTO logDTO = new LogDTO();
            logDTO.setType(LogConst.TYPE_ERROR);
            logDTO.setDetail(String.valueOf(e));
            mapper.insert(logDTO);
            log.error( e.getMessage() );
        }
    }
}
