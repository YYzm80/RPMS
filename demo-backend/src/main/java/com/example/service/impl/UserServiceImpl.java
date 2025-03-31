package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.dto.auth.Account;
import com.example.entity.dto.in.UserImportDTO;
import com.example.entity.vo.response.AccountVO;
import com.example.mapper.AccountMapper;
import com.example.service.DataService;
import com.example.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl extends ServiceImpl<AccountMapper, Account> implements UserService, DataService<UserImportDTO> {

    @Resource
    AccountMapper mapper;

    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public List<AccountVO> getUserList() {
        List<Account> accounts = mapper.selectList(null);
        return accounts.stream()
                .map(account -> convert(account, false))
                .toList();
    }

    @Override
    public List<AccountVO> getOwnerList() {
        return mapper.findAccountsOwner();
    }

    @Override
    public List<AccountVO> getPropertyOwnerList() {
        return mapper.findAccountsProperty();
    }

    @Override
    public AccountVO getUserByUid(Integer uid) {
        Account account = mapper.selectById(uid);
        return convert(account, true);
    }

    @Override
    public String addUser(Account account) {
        account.setPassword(encoder.encode("123456"));
        if (Objects.equals(account.getUsername(), mapper.findAccountByNameOrEmail(account.getUsername()).getUsername())) {
            return "用户名已存在，请重新输入";
        }
        return mapper.insert(account) > 0 ? null : "添加用户失败，请稍后再试";
    }

    @Override
    public String updateUser(Account account) {
        if (mapper.findAccountByNameOrEmail(account.getUsername()) != null) {
            Long uid = mapper.findAccountByNameOrEmail(account.getUsername()).getUserId();
            if (!Objects.equals(account.getUserId(), uid)) {
                return "用户名已存在，请重新输入";
            }
        }
        return mapper.updateById(account) > 0 ? null : "更新个人资料失败，请稍后再试";
    }

    @Override
    public String deleteUserByUid(Integer uid) {
        return mapper.deleteById(uid) > 0 ? null : "删除用户失败，请稍后再试";
    }

    @Override
    public Integer changeStatus(Long uid) {
        Account account = mapper.selectById(uid);
        account.setStatus(account.getStatus().equals("active") ? "inactive" : "active");
        if (account.getStatus().equals("active")) {
            return mapper.updateById(account) > 0 ? 1 : 3;
        } else {
            return mapper.updateById(account) > 0 ? 2 : 3;
        }
    }

    @Override
    public void batchImport(List<UserImportDTO> list) {
        List<Account> entities = list.stream()
                .map(dto -> {
                    Account user = new Account();
                    BeanUtils.copyProperties(dto, user);
                    user.setPassword(encoder.encode("123456")); // 默认密码
                    user.setStatus("active");
                    user.setCreatedAt(new Date());
                    return user;
                })
                .collect(Collectors.toList());

        mapper.insertBatchSomeColumn(entities);
    }

    private AccountVO convert(Account account, boolean isForm) {
        return account.asViewObject(AccountVO.class, v -> {
            if (!isForm) {
                v.setRoleName(account.getRid() == 1 ? "系统管理员" :
                        account.getRid() == 2 ? "物业人员" : "业主");
                v.setGender(account.getGender().equals("male") ? "男" :
                        account.getGender().equals("female") ? "女" : "其它");
                v.setStatus(account.getStatus().equals("active") ? "正常" : "封禁");
            } else {
                v.setRoleName(String.valueOf(account.getRid()));
            }
        });
    }

}
