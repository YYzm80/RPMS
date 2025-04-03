package com.example.controller;

import com.example.entity.RestBean;
import com.example.entity.vo.request.email.ConfirmResetVO;
import com.example.entity.vo.request.email.EmailResetVO;
import com.example.service.AuthorizeService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/api/auth")
public class AuthorizeController {

    @Resource
    AuthorizeService service;

    @Operation(summary = "发送验证码")
    @PostMapping("/ask-code")
    public RestBean<String> askVerifyCode(@RequestParam @Email String email,
                                          HttpSession session) {
        String s = service.sendValidateEmail(email, session.getId(), true);

        if (s == null)
            return RestBean.success("邮件发送成功，请注意查收");
        else
            return RestBean.failure(400, s);
    }

    /**
     * 执行密码重置确认，检查验证码是否正确
     *
     * @param vo 密码重置信息
     * @return 是否操作成功
     */
    @Operation(summary = "验证邮箱验证码")
    @PostMapping("/reset-confirm")
    public RestBean<String> resetConfirm(@RequestBody @Valid ConfirmResetVO vo,
                                         HttpSession session) {
        String s = service.validateOnly(vo.getEmail(), vo.getCode(), session.getId());
        if (s == null) {
            session.setAttribute("reset-password", vo.getEmail());
            return RestBean.success();
        } else {
            return RestBean.failure(401, s);
        }
    }

    /**
     * 执行密码重置操作
     *
     * @param vo 密码重置信息
     * @return 是否操作成功
     */
    @Operation(summary = "重置密码")
    @PostMapping("/reset-password")
    public RestBean<String> resetPassword(@RequestBody @Valid EmailResetVO vo,
                                          HttpSession session) {
        String email = (String) session.getAttribute("reset-password");
        if (email == null) {
            return RestBean.failure(401, "请先完成邮箱验证");
        } else if (service.resetPassword(vo.getPassword(), email)) {
            session.removeAttribute("reset-password");
            return RestBean.success("密码重置成功");
        } else {
            return RestBean.failure(500, "内部错误，请联系管理员");
        }
    }

}
