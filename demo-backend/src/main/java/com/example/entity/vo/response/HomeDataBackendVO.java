package com.example.entity.vo.response;

import com.example.entity.dto.stat.DailyWorkStat;
import com.example.entity.dto.stat.PaymentTypeStat;
import lombok.Data;

import java.util.List;

@Data
public class HomeDataBackendVO {
    private Integer newComplaintsAndRepairsToday;
    private Integer unsolvedComplaintsAndRepairs;
    private Integer solvingComplaintsAndRepairs;
    private Integer mySolvedComplaintsAndRepairs;
    private List<DailyWorkStat> dailyWorkStatList;
    private List<PaymentTypeStat> paymentTypeStatList;
}
