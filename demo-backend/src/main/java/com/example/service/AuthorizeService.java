package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.dto.auth.Account;
import com.example.entity.vo.response.AccountVO;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface AuthorizeService extends UserDetailsService, IService<Account> {
    String sendValidateEmail(String email, String sessionId, boolean hasAccount);
    String validateOnly(String email, String code, String sessionId);
    Account findAccountByNameOrEmail(String text);
    boolean resetPassword(String password, String email);
}
