package com.example.entity.dto.stat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 社区基本数据统计
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyBasicCommunityStat {
    /**
     * 居住人数
     */
    private Integer livingCount;
    /**
     * 房产空置率
     */
    private Double emptyPropertyRate;
    /**
     * 投诉数量
     */
    private Integer complaintCount;
    /**
     * 报修数量
     */
    private Integer repairCount;
}
