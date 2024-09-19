package com.example.controller;

import com.example.entity.RestBean;
import com.example.entity.dto.game.Registration;
import com.example.entity.vo.response.RegistrationVO;
import com.example.service.RegistrationService;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/registration")
public class RegistrationController {

    @Resource
    private RegistrationService service;

    @GetMapping("/all")
    public RestBean<List<RegistrationVO>> all() {
        return RestBean.success(service.getAllRegistrations());
    }

    @GetMapping("/all-state")
    public RestBean<List<RegistrationVO>> state(String state) {
        return RestBean.success(service.getAllRegistrationsByState(state));
    }

    @GetMapping("/all-uid")
    public RestBean<List<RegistrationVO>> uid(Integer uid) {
        return RestBean.success(service.getRegistrationByUid(uid));
    }

    @GetMapping("/all-gid")
    public RestBean<List<RegistrationVO>> gid(Integer gid) {
        return RestBean.success(service.getRegistrationByGid(gid));
    }

    @GetMapping("/rid")
    public RestBean<RegistrationVO> rid(Integer rid) {
        return RestBean.success(service.getRegistrationByRid(rid));
    }

    @PostMapping("/add")
    public RestBean<String> add(Registration registration) {
        String s = service.addRegistration(registration);
        return s == null ? RestBean.success("报名成功，请等待管理员审核") : RestBean.failure(400, s);
    }

    @PostMapping("/update")
    @PreAuthorize("hasRole('admin')")
    public RestBean<String> update(Registration registration) {
        String s = service.updateRegistration(registration);
        return s.equals("1") ? RestBean.success("审核成功，该报名已通过") :
                s.equals("2") ? RestBean.success("审核成功，该报名未通过") :
                        RestBean.failure(400, "审核失败，请联系管理员");
    }

    @PostMapping("/delete")
    public RestBean<String> delete(Integer rid) {
        String s = service.deleteRegistrationByRid(rid);
        return s == null ? RestBean.success("取消报名成功") : RestBean.failure(400, s);
    }

}
