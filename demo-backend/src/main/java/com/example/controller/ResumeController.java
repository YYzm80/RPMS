package com.example.controller;

import com.example.entity.RestBean;
import com.example.entity.dto.common.Resume;
import com.example.entity.vo.response.ResumeVO;
import com.example.service.ResumeService;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/resume")
public class ResumeController {
    @Resource
    ResumeService service;

    @GetMapping("/all-uid/{uid}")
    public RestBean<List<ResumeVO>> uid(@PathVariable("uid") Integer uid) {
        return RestBean.success(service.getAllResumesByUid(uid));
    }

    @GetMapping("/{rid}")
    public RestBean<ResumeVO> rid(@PathVariable("rid") Integer rid) {
        return RestBean.success(service.getResumesByRid(rid));
    }

    @PostMapping("/add")
    @PreAuthorize("hasAnyRole('admin', 'student')")
    public RestBean<String> add(@RequestParam("filePDF") MultipartFile file, Resume resume) {
        String s = service.addResume(file, resume);
        return s == null ? RestBean.success("上传简历成功") : RestBean.failure(400, s);
    }

    @PostMapping("/delete")
    @PreAuthorize("hasAnyRole('admin', 'student')")
    public RestBean<String> delete(Integer rid) {
        String s = service.deleteResume(rid);
        return s == null ? RestBean.success("删除简历成功") : RestBean.failure(400, s);
    }

}
