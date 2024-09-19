package com.example.controller;

import com.example.entity.RestBean;
import com.example.entity.vo.request.email.ConfirmResetVO;
import com.example.entity.vo.request.email.EmailRegisterVO;
import com.example.entity.vo.request.email.EmailResetVO;
import com.example.service.AuthorizeService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@Validated
@RestController
@RequestMapping("/api/auth")
public class AuthorizeController {

    @Resource
    AuthorizeService service;

    @PostMapping("/ask-code")
    public RestBean<String> askVerifyCode(@RequestParam @Email String email,
                                        @RequestParam @Pattern(regexp = "(register|reset)")  String type,
                                        HttpSession session){
        String s = Objects.equals(type, "register") ?
                service.sendValidateEmail(email, session.getId(), false) :
                service.sendValidateEmail(email, session.getId(), true);

        if (s == null)
            return RestBean.success("邮件发送成功，请注意查收");
        else
            return RestBean.failure(400, s);
    }

    /**
     * 进行用户注册操作，需要先请求邮件验证码
     * @param vo 注册信息
     * @return 是否注册成功
     */
    @PostMapping("/register")
    public RestBean<String> register(@RequestBody @Valid EmailRegisterVO vo,
                                     HttpSession session){
        String s = service.validateAndRegister(vo.getUsername(), vo.getPassword(), vo.getEmail(), vo.getCode(), session.getId());
        if (s == null) {
            return RestBean.success("注册成功");
        } else {
            return RestBean.failure(400, s);
        }
    }

    /**
     * 执行密码重置确认，检查验证码是否正确
     * @param vo 密码重置信息
     * @return 是否操作成功
     */
    @PostMapping("/reset-confirm")
    public RestBean<String> resetConfirm(@RequestBody @Valid ConfirmResetVO vo,
                                        HttpSession session){
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
     * @param vo 密码重置信息
     * @return 是否操作成功
     */
    @PostMapping("/reset-password")
    public RestBean<String> resetPassword(@RequestBody @Valid EmailResetVO vo,
                                          HttpSession session){
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
