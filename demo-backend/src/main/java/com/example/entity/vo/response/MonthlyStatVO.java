package com.example.entity.vo.response;

import com.example.entity.dto.stat.MonthlyBasicCommunityStat;
import com.example.entity.dto.stat.MonthlyIncomeStat;
import com.example.entity.dto.stat.MonthlyManagerWorkStat;
import lombok.Data;

import java.util.Date;

@Data
public class MonthlyStatVO {
    private Long id;
    private String month;
    private MonthlyBasicCommunityStat basicCommunityStat;
    private MonthlyManagerWorkStat managerWorkStat;
    private MonthlyIncomeStat incomeStat;
    private Date generateTime;
}
