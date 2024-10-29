package com.example.entity.vo.response;

import com.example.entity.dto.common.PopularCompany;
import lombok.Data;

import java.util.List;

@Data
public class HomeStatic {
    private Long userCount;
    private Long companyCount;
    private Long meetingCount;
    private Long meetingRunCount;
    private List<PopularCompany> popularCompanies;
}
