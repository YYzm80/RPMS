package com.example.entity.dto.stat;

import com.example.entity.BaseData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyStat implements BaseData {
    private MonthlyBasicCommunityStat basicCommunityStat;
    private MonthlyManagerWorkStat managerWorkStat;
    private MonthlyIncomeState incomeState;
}
