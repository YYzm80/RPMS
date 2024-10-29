package com.example.controller;

import com.example.entity.RestBean;
import com.example.entity.dto.interview.Interview;
import com.example.entity.vo.response.InterviewVO;
import com.example.service.InterviewService;
import jakarta.annotation.Resource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/interview")
public class InterviewController {
    @Resource
    private InterviewService service;

    @GetMapping("/all-uid/{uid}")
    public RestBean<List<InterviewVO>> allUid(@PathVariable("uid") Integer uid) {
        return RestBean.success(service.getAllByUid(uid));
    }

    @GetMapping("/all-cid/{cid}")
    public RestBean<List<InterviewVO>> allCid(@PathVariable("cid") Integer cid) {
        return RestBean.success(service.getAllByCid(cid));
    }

    @GetMapping("/{iid}")
    public RestBean<InterviewVO> Iid(@PathVariable("iid") Integer iid) {
        return RestBean.success(service.getByIid(iid));
    }

    @PostMapping("/add")
    @PreAuthorize("hasAnyRole('admin', 'company')")
    public RestBean<String> add(Interview interview) {
        String s = service.addInterview(interview);
        return s == null ? RestBean.success("发出面试成功") : RestBean.failure(400, s);
    }

    @PostMapping("/update")
    @PreAuthorize("hasAnyRole('admin', 'company')")
    public RestBean<String> update(Interview interview) {
        String s = service.updateInterview(interview);
        return s == null ? RestBean.success("更新面试状态成功") : RestBean.failure(400, s);
    }

    @PostMapping("/delete")
    @PreAuthorize("hasAnyRole('admin', 'company')")
    public RestBean<String> delete(Integer iid) {
        String s = service.deleteInterviewByIid(iid);
        return s == null ? RestBean.success("取消面试成功") : RestBean.failure(400, s);
    }

}
