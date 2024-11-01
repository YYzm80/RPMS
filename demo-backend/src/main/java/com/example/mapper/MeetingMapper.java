package com.example.mapper;

import com.example.config.MyBaseMapper;
import com.example.entity.dto.meeting.Meeting;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MeetingMapper extends MyBaseMapper<Meeting> {
}
