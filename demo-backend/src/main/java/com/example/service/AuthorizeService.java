package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.dto.auth.Account;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AuthorizeService extends UserDetailsService, IService<Account> {
    String sendValidateEmail(String email, String sessionId, boolean hasAccount);

    String validateAndRegister(String username, String password, String email, String code, String sessionId);

    String validateOnly(String email, String code, String sessionId);

    Account findAccountByNameOrEmail(String text);

    boolean resetPassword(String password, String email);
}
