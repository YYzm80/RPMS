package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.dto.auth.Account;
import com.example.entity.vo.response.AccountVO;
import org.springframework.web.multipart.MultipartFile;

public interface UserService extends IService<Account> {
    AccountVO getUserByUid(Integer uid);
    String updateUser(Account account, MultipartFile file);
    String deleteUserByUid(Integer uid);
}
