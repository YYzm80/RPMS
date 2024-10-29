package com.example.controller;

import com.example.entity.RestBean;
import com.example.entity.dto.auth.Account;
import com.example.entity.vo.response.AccountVO;
import com.example.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Resource
    private UserService service;

    @GetMapping("/all")
    public RestBean<List<AccountVO>> all() {
        return RestBean.success();
    }

    @GetMapping("/uid")
    public RestBean<AccountVO> get(Integer uid) {
        return RestBean.success(service.getUserByUid(uid));
    }

    @PostMapping("/update")
    public RestBean<String> update(@RequestParam(value = "file", required = false) MultipartFile file,
                                   Account account) {
        String s = service.updateUser(account, file);
        return s == null ? RestBean.success("个人资料更新成功，新的头像图像将在您下次登录后生效显示") : RestBean.failure(400, s);
    }

    @PostMapping("/delete")
    @PreAuthorize("hasRole('admin')")
    public RestBean<String> delete(Integer uid) {
        String s = service.deleteUserByUid(uid);
        return s == null ? RestBean.success("用户删除成功") : RestBean.failure(400, s);
    }

}
