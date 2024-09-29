package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.dto.auth.Account;
import com.example.entity.vo.response.AccountVO;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface AuthorizeService extends UserDetailsService, IService<Account> {
    String sendValidateEmail(String email, String sessionId, boolean hasAccount);
    String validateAndRegister(String username, String password, String email, String code, String role, String sessionId);
    String validateOnly(String email, String code, String sessionId);
    Account findAccountByNameOrEmail(String text);
    boolean resetPassword(String password, String email);
    List<AccountVO> getAllUsers();
    AccountVO getUserByUid(Integer uid);
    String updateUser(Account account);
    String deleteUserByUid(Integer uid);
}
