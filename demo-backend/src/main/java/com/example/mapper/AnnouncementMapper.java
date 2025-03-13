package com.example.mapper;

import com.example.common.config.MyBaseMapper;
import com.example.entity.dto.common.Announcement;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AnnouncementMapper extends MyBaseMapper<Announcement> {
}
