package com.example.entity.dto.stat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 每日工作统计实体（用于近7日报修/投诉处理量统计）
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DailyWorkStat {
    /**
     * 统计日期（格式：yyyy-MM-dd）
     */
    private String date;

    /**
     * 当日完成的报修工单数量
     */
    private Integer repairCompletedCount;

    /**
     * 当日完成的投诉处理数量
     */
    private Integer complaintResolvedCount;
}
