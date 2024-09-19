package com.example.controller;

import com.example.entity.RestBean;
import com.example.entity.dto.auth.Account;
import com.example.entity.vo.response.AccountVO;
import com.example.service.AuthorizeService;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Resource
    private AuthorizeService service;

    @GetMapping("/all")
    public RestBean<List<AccountVO>> all() {
        return RestBean.success(service.getAllUsers());
    }

    @GetMapping("/uid")
    public RestBean<AccountVO> get(Integer uid) {
        return RestBean.success(service.getUserByUid(uid));
    }

    @PostMapping("/update")
    @PreAuthorize("hasRole('admin')")
    public RestBean<String> update(Account account) {
        String s = service.updateUser(account);
        return s == null ? RestBean.success("用户信息修改成功") : RestBean.failure(400, s);
    }

    @PostMapping("/delete")
    @PreAuthorize("hasRole('admin')")
    public RestBean<String> delete(Integer uid) {
        String s = service.deleteUserByUid(uid);
        return s == null ? RestBean.success("用户删除成功") : RestBean.failure(400, s);
    }

}
