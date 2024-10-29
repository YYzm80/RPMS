package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.dto.meeting.Meeting;
import com.example.entity.vo.response.MeetingVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MeetingService extends IService<Meeting> {
    List<MeetingVO> getAllMeeting();
    MeetingVO getMeetingByMid(Integer mid);
    String addMeeting(MultipartFile file, Meeting meeting);
    String updateMeeting(MultipartFile file, Meeting meeting);
    void updateMeetingState();
    String deleteMeetingByMid(Integer mid);
}
