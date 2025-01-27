package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.dto.auth.Account;
import com.example.entity.vo.response.AccountVO;
import com.example.mapper.AccountMapper;
import com.example.service.UserService;
import com.example.util.Const;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class UserServiceImpl  extends ServiceImpl<AccountMapper, Account> implements UserService {

    @Resource
    AccountMapper mapper;


    @Override
    public AccountVO getUserByUid(Integer uid) {
        return mapper.selectById(uid).asViewObject(AccountVO.class);
    }

    @Override
    public String updateUser(Account account, MultipartFile file) {
        if (file != null && !file.isEmpty()) {
            try {
                String fileName = System.currentTimeMillis() + file.getOriginalFilename();
                Path uploadFilePath = Paths.get(Const.UPLOAD_PATH, fileName);
                Files.createDirectories(uploadFilePath.getParent());
                // 如果用户头像已存在，则删除旧头像
                if (mapper.selectById(account.getUid()).getAvatar() != null) {
                    String oldFileName = mapper.selectById(account.getUid()).getAvatar();
                    Path oldFilePath = Paths.get(Const.UPLOAD_PATH, oldFileName);
                    if (oldFileName != null && !oldFileName.isEmpty()) {
                        Files.deleteIfExists(oldFilePath);
                    }
                }
                file.transferTo(uploadFilePath.toFile());
                account.setAvatar(fileName);
            } catch (IOException e) {
                e.printStackTrace();
                return "上传头像失败，请稍后再试";
            }
        }
        return mapper.updateById(account) > 0 ? null : "更新个人资料失败，请稍后再试";
    }

    @Override
    public String deleteUserByUid(Integer uid) {
        return mapper.deleteById(uid) > 0 ? null : "删除用户失败，请稍后再试";
    }
}
