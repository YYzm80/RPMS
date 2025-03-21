package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.dto.auth.Account;
import com.example.mapper.AccountMapper;
import com.example.mapper.RoleMapper;
import com.example.service.AuthorizeService;
import com.example.util.HttpContextUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class AuthorizeServiceImpl extends ServiceImpl<AccountMapper, Account> implements AuthorizeService {

    @Value("${spring.mail.username}")
    String from;

    @Resource
    AccountMapper mapper;

    @Resource
    RoleMapper roleMapper;

    @Resource
    MailSender mailSender;

    @Resource
    StringRedisTemplate template;

    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = mapper.findAccountByNameOrEmail(username);
        if (account == null)
            throw new UsernameNotFoundException("用户名或密码错误！");
        if (account.getStatus().equals("inactive")) {
            throw new LockedException("您的账号已被封禁，请联系管理员！");
        }
        return User
                .withUsername(account.getUsername())
                .password(account.getPassword())
                .roles(roleMapper.selectById(account.getRid()).getName())
                .build();
    }

    @Override
    public String sendValidateEmail(String email, String sessionId, boolean hasAccount) {
        String ipKey = "ip:" + HttpContextUtils.getIpAddress();
        Long ipRequestCount = Optional.ofNullable(template.opsForValue().get(ipKey)).map(Long::parseLong).orElse(0L);
        if (ipRequestCount >= 10) { // 假设每个 IP 每分钟最多请求 10 次
            return "请求过于频繁，请稍后再试！";
        }

        String key = "email:" + sessionId + ":" + email + ":" + hasAccount;
        if (Boolean.TRUE.equals(template.hasKey(key))) {
            Long expire = Optional.ofNullable(template.getExpire(key, TimeUnit.SECONDS)).orElse(0L);
            if (expire > 120) return "请求频繁，请稍后再试！";
        }
        Account account = mapper.findAccountByNameOrEmail(email);
        if (hasAccount && account == null) return "没有此邮件的账户";
        if (!hasAccount && account != null) return "此邮箱已被其他用户注册";
        Random random = new Random();
        int code = random.nextInt(899999) + 100000;
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setSubject("您的验证邮件");
        message.setText("验证码是：" + code + "，有效时间为3分钟");
        message.setTo(email);
        try {
            mailSender.send(message);
            template.opsForValue().set(key, String.valueOf(code), 3, TimeUnit.MINUTES);
            return null;
        } catch (MailException e) {
            log.error("邮件发送失败", e);
            return "邮件发送失败，请检查邮箱地址是否有效";
        }
    }

    @Override
    public String validateOnly(String email, String code, String sessionId) {
        String key = "email:" + sessionId + ":" + email + ":true";
        if (Boolean.TRUE.equals(template.hasKey(key))) {
            String s = template.opsForValue().get(key);
            if (s == null) return "验证码失效，请重新请求验证码";
            if (s.equals(code)) {
                template.delete(key);
                return null;
            } else {
                return "验证码错误，请检查后重新提交";
            }
        } else {
            return "请先完成获取验证码！";
        }
    }

    @Override
    public Account findAccountByNameOrEmail(String text) {
        return mapper.findAccountByNameOrEmail(text);
    }

    @Override
    public boolean resetPassword(String password, String email) {
        password = encoder.encode(password);
        return mapper.resetPasswordByEmail(password, email) > 0;
    }


}
