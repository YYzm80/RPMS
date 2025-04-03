package com.example.entity.vo.response;

import lombok.Data;

import java.util.List;

@Data
public class HomeDataVO {
    private Long userCount;
    private Long lastMonthUserCount;
    private Long emptyPropertyCount;
    private Long lastMonthEmptyPropertyCount;
    private List<AnnouncementVO> announcements;
}
