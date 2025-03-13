package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.dto.auth.Account;
import com.example.entity.dto.in.UserImportDTO;
import com.example.entity.vo.response.AccountVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService extends IService<Account> {
    List<AccountVO> getUserList();
    List<AccountVO> getOwnerList();
    AccountVO getUserByUid(Integer uid);
    String addUser(Account account);
    String updateUser(Account account);
    String deleteUserByUid(Integer uid);
    Integer changeStatus(Long uid);
}
