package com.example.controller;

import com.example.entity.RestBean;
import com.example.entity.dto.game.Arrangement;
import com.example.entity.vo.response.ArrangementVO;
import com.example.service.ArrangementService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/arrangement")
public class ArrangementController {

    @Resource
    private ArrangementService service;

    @GetMapping("/all-gid")
    public RestBean<List<ArrangementVO>> gid(Integer gid) {
        return RestBean.success(service.getArrangementByGid(gid));
    }

    @GetMapping("/all-uid")
    public RestBean<List<ArrangementVO>> uid(Integer uid) {
        return RestBean.success(service.getArrangementByUid(uid));
    }

    @GetMapping("/aid")
    public RestBean<ArrangementVO> aid(Integer aid) {
        return RestBean.success(service.getArrangementByAid(aid));
    }

    @PostMapping("/doArrangement")
    public RestBean<String> doArrangement(Integer gid) {
        String s = service.doArrangement(gid);
        return s == null ? RestBean.success("自动安排完成，请查看详情确认") : RestBean.failure(400, s);
    }

    @PostMapping("/update")
    public RestBean<String> update(Arrangement arrangement) {
        String s = service.updateArrangement(arrangement);
        return s == null ? RestBean.success("安排信息更新成功") : RestBean.failure(400, s);
    }

    @PostMapping("/delete")
    public RestBean<String> delete(Integer aid) {
        String s = service.deleteArrangementByAid(aid);
        return s == null ? RestBean.success("安排信息删除成功") : RestBean.failure(400, s);
    }
}
