package com.example.controller;

import com.example.entity.RestBean;
import com.example.entity.dto.meeting.Meeting;
import com.example.entity.vo.response.MeetingVO;
import com.example.service.MeetingService;
import jakarta.annotation.Resource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/meeting")
public class MeetingController {
    @Resource
    private MeetingService service;

    @GetMapping("/all")
    public RestBean<List<MeetingVO>> all() {
        return RestBean.success(service.getAllMeeting());
    }

    @GetMapping("/{mid}")
    public RestBean<MeetingVO> mid(@PathVariable("mid") Integer mid) {
        return RestBean.success(service.getMeetingByMid(mid));
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('admin')")
    public RestBean<String> add(@RequestParam("file") MultipartFile file, Meeting meeting) {
        String s = service.addMeeting(file, meeting);
        return s == null ? RestBean.success("新增双选会信息成功") : RestBean.failure(400, s);
    }

    @PostMapping("/update")
    @PreAuthorize("hasRole('admin')")
    public RestBean<String> update(@RequestParam(value = "file", required = false) MultipartFile file,
                                   Meeting meeting) {
        String s = service.updateMeeting(file, meeting);
        return s == null ? RestBean.success("更新双选会信息成功") : RestBean.failure(400, s);
    }

    @PostMapping("/delete")
    @PreAuthorize("hasRole('admin')")
    public RestBean<String> delete(Integer mid) {
        String s = service.deleteMeetingByMid(mid);
        return s == null ? RestBean.success("删除双选会信息成功") : RestBean.failure(400, s);
    }

    @Scheduled(fixedDelay = 1000*60*60)
    public void updateMeeting() {
        System.out.println("定时器开始执行");
        service.updateMeetingState();
        System.out.println("定时器执行完成，meeting状态更新完成");
    }
}
