package com.example.controller;

import com.example.entity.RestBean;
import com.example.entity.dto.common.Tags;
import com.example.service.TagsService;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
public class TagsController {
    @Resource
    private TagsService service;

    @GetMapping("/all")
    public RestBean<List<Tags>> all() {
        return RestBean.success(service.getAllTags());
    }

    @GetMapping("/{tid}")
    public RestBean<Tags> get(@PathVariable("tid") Integer tid) {
        return RestBean.success(service.getTagByTid(tid));
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('admin')")
    public RestBean<String> add(Tags tags) {
        String s = service.addTag(tags);
        return s == null ? RestBean.success("新增标签成功") : RestBean.failure(400, s);
    }

    @PostMapping("/update")
    @PreAuthorize("hasRole('admin')")
    public RestBean<String> update(Tags tags) {
        String s = service.updateTag(tags);
        return s == null ? RestBean.success("更新标签成功") : RestBean.failure(400, s);
    }

    @PostMapping("/delete")
    @PreAuthorize("hasRole('admin')")
    public RestBean<String> delete(Integer tid) {
        String s = service.deleteTagByTid(tid);
        return s == null ? RestBean.success("删除标签成功") : RestBean.failure(400, s);
    }
}
