package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.dto.auth.Account;
import com.example.entity.dto.game.Registration;
import com.example.entity.vo.response.AccountVO;
import com.example.mapper.AcademyMapper;
import com.example.mapper.AccountMapper;
import com.example.mapper.ClassMapper;
import com.example.mapper.RegistrationMapper;
import com.example.service.AuthorizeService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class AuthorizeServiceImpl extends ServiceImpl<AccountMapper, Account> implements AuthorizeService {

    @Value("${spring.mail.username}")
    String from;

    @Resource
    AccountMapper mapper;

    @Resource
    AcademyMapper academyMapper;

    @Resource
    ClassMapper classMapper;

    @Resource
    RegistrationMapper registrationMapper;

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
        return User
                .withUsername(account.getUsername())
                .password(account.getPassword())
                .roles(account.getRole())
                .build();
    }

    @Override
    public String sendValidateEmail(String email, String sessionId, boolean hasAccount) {
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
            e.printStackTrace();
            return "邮件发送失败，请检查邮箱地址是否有效";
        }
    }

    @Override
    public String validateAndRegister(String username, String password, String email, String code, String role, String sessionId) {
        String key = "email:" + sessionId + ":" + email + ":false";
        if (Boolean.TRUE.equals(template.hasKey(key))) {
            String s = template.opsForValue().get(key);
            if (s == null) return "验证码失效，请重新请求验证码";
            if (s.equals(code)) {
                Account account = mapper.findAccountByNameOrEmail(username);
                if (account != null) return "此用户名已被注册";
                template.delete(key);
                password = encoder.encode(password);
                if (mapper.createAccount(username, password, email, role) > 0) {
                    return null;
                } else {
                    return "内部错误，请联系管理员";
                }
            } else {
                return "验证码错误，请检查后重新提交";
            }
        } else {
            return "请先完成获取验证码！";
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

    @Override
    public List<AccountVO> getAllUsers() {
        List<Account> accounts = mapper.selectList(null);
        List<AccountVO> vos = new ArrayList<>();
        accounts.forEach(account -> {
            AccountVO vo = account.asViewObject(AccountVO.class, v -> {
                if (account.getAid() != null && account.getCid() != null) {
                    v.setAcademy(academyMapper.getAcademyByAId(account.getAid()).getName());
                    v.setClazz(classMapper.selectById(account.getCid()).getName());
                } else {
                    v.setAcademy("-");
                    v.setClazz("-");
                }
            });
            vos.add(vo);
        });
        return vos;
    }

    @Override
    public AccountVO getUserByUid(Integer uid) {
        return mapper.selectById(uid).asViewObject(AccountVO.class);
    }

    @Override
    public String updateUser(Account account) {
        return mapper.updateById(account) > 0 ? null : "修改用户信息失败，请稍后再试";
    }

    @Override
    public String deleteUserByUid(Integer uid) {
        if (registrationMapper.selectList(new QueryWrapper<Registration>().eq("uid", uid)).isEmpty())
            return mapper.deleteById(uid) > 0 ? null : "删除用户失败，请稍后再试";
        return "删除用户失败，请先删除该用户的其它信息";
    }
}
