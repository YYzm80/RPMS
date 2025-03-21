package com.example.controller;

import com.example.entity.RestBean;
import com.example.entity.dto.common.Announcement;
import com.example.entity.vo.response.AnnouncementVO;
import com.example.service.AnnounceService;
import com.example.util.consts.Const;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/announce")
public class AnnounceController {

    @Resource
    private AnnounceService service;

    @Operation(summary = "获取所有公告")
    @GetMapping("/all")
    public RestBean<List<AnnouncementVO>> all() {
        return RestBean.success(service.getAnnouncementList());
    }

    @Operation(summary = "获取所有已发布的公告")
    @GetMapping("/all-published")
    public RestBean<List<AnnouncementVO>> get() {
        return RestBean.success(service.getPublishAnnouncementList());
    }

    @Operation(summary = "获取公告详情")
    @GetMapping("/aid/{aid}")
    public RestBean<AnnouncementVO> get(@PathVariable("aid") Long aid) {
        return RestBean.success(service.getAnnouncementById(aid));
    }

    @Operation(summary = "发布公告")
    @RolesAllowed({Const.ROLE_ADMIN, Const.ROLE_MANAGER})
    @PostMapping("/add")
    public RestBean<String> add(@RequestBody @Valid Announcement announcement) {
        String s = service.addAnnouncement(announcement);
        return s == null ? RestBean.success("公告发布成功") : RestBean.failure(400, s);
    }

    @Operation(summary = "更新公告")
    @RolesAllowed({Const.ROLE_ADMIN, Const.ROLE_MANAGER})
    @PutMapping ("/update")
    public RestBean<String> update(@RequestBody @Valid Announcement announcement) {
        String s = service.updateAnnouncement(announcement);
        return s == null ? RestBean.success("公告更新成功") : RestBean.failure(400, s);
    }

    @Operation(summary = "删除公告")
    @RolesAllowed({Const.ROLE_ADMIN, Const.ROLE_MANAGER})
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
