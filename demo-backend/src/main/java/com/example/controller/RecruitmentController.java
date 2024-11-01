package com.example.controller;

import com.example.entity.RestBean;
import com.example.entity.dto.meeting.Recruitment;
import com.example.entity.vo.response.RecruitmentVO;
import com.example.service.RecruitmentService;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recruitment")
public class RecruitmentController {
    @Resource
    private RecruitmentService service;

    @GetMapping("/all-mid/{mid}")
    public RestBean<List<RecruitmentVO>> allMid(@PathVariable("mid") Integer mid) {
        return RestBean.success(service.getAllRecruitmentByMid(mid));
    }

    @GetMapping("/all-cid/{cid}")
    public RestBean<List<RecruitmentVO>> allCid(@PathVariable("cid") Integer cid) {
        return RestBean.success(service.getAllRecruitmentByCid(cid));
    }

    @GetMapping("/{rid}")
    public RestBean<RecruitmentVO> rid(@PathVariable("rid") Integer rid) {
        return RestBean.success(service.getRecruitmentByRid(rid));
    }

    @PostMapping("/add")
    @PreAuthorize("hasAnyRole('admin', 'company')")
    public RestBean<String> add(Recruitment recruitment,
                                @RequestParam("tidList") List<Integer> tidList) {
        String s = service.addRecruitment(recruitment, tidList);
        return s == null ? RestBean.success("发布岗位成功") : RestBean.failure(400, s);
    }

    @PostMapping("/update")
    @PreAuthorize("hasAnyRole('admin', 'company')")
    public RestBean<String> update(Recruitment recruitment,
                                   @RequestParam("tidList") List<Integer> tidList) {
        String s = service.updateRecruitment(recruitment, tidList);
        return s == null ? RestBean.success("更新岗位信息成功") : RestBean.failure(400, s);
    }

    @PostMapping("/delete")
    @PreAuthorize("hasAnyRole('admin', 'company')")
    public RestBean<String> delete(Integer rid) {
        String s = service.deleteRecruitment(rid);
        return s == null ? RestBean.success("删除岗位成功") : RestBean.failure(400, s);
    }
}
