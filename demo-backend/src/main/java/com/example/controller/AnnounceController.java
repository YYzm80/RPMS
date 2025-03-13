package com.example.controller;

import com.example.entity.RestBean;
import com.example.entity.dto.common.Announcement;
import com.example.entity.vo.response.AnnouncementVO;
import com.example.service.AnnounceService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/announce")
public class AnnounceController {

    @Resource
    private AnnounceService service;

    @GetMapping("/all")
    public RestBean<List<AnnouncementVO>> all() {
        return RestBean.success(service.getAnnouncementList());
    }

    @GetMapping("/all-published")
    public RestBean<List<AnnouncementVO>> get() {
        return RestBean.success(service.getPublishAnnouncementList());
    }

    @GetMapping("/aid/{aid}")
    public RestBean<AnnouncementVO> get(@PathVariable("aid") Long aid) {
        return RestBean.success(service.getAnnouncementById(aid));
    }

    @PostMapping("/add")
    public RestBean<String> add(@RequestBody Announcement announcement) {
        String s = service.addAnnouncement(announcement);
        return s == null ? RestBean.success("公告发布成功") : RestBean.failure(400, s);
    }

    @PostMapping("/update")
    public RestBean<String> update(@RequestBody Announcement announcement) {
        String s = service.updateAnnouncement(announcement);
        return s == null ? RestBean.success("公告更新成功") : RestBean.failure(400, s);
    }

    @PostMapping("/delete")
    public RestBean<String> delete(@RequestBody Long aid) {
        Integer i = service.deleteAnnouncement(aid);
        return switch (i) {
            case 1 -> RestBean.success("公告已彻底删除成功");
            case 2 -> RestBean.success("公告已暂时删除，再次删除将会彻底删除");
            case 3, 4 -> RestBean.failure(400, "公告删除失败，请稍后再试");
            default -> RestBean.failure(400, "未知错误");
        };
    }

}
