package com.example.entity.dto.stat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 物业工作统计
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyManagerWorkStat {
    /**
     * 物业人员数量
     */
    private Integer ManagerCount;
    /**
     * 投诉解决数量
     */
    private Integer resolvedComplaintsCount;
    /**
     * 投诉解决率
     */
    private Double complaintResolvedRate;
    /**
     * 报修解决数量
     */
    private Integer solvedRepairsCount;
    /**
     * 报修解决率
     */
    private Double repairSolvedRate;
    /**
     * 发布公告数量
     */
    private Integer publishedAnnouncementCount;
}
