package com.example.filter;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.entity.dto.auth.Account;
import com.example.entity.dto.in.UserImportDTO;
import com.example.service.UserService;
import com.example.util.ErrorRecorder;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

// 用户导入过滤器实现
@Component("userImportFilter")
public class UserImportFilter implements ExcelDataFilter<UserImportDTO> {

    private final UserService userService;

    @Autowired
    public UserImportFilter(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean filter(UserImportDTO data, Integer rowIndex) {
        // 基础校验（JSR303）
        Set<ConstraintViolation<UserImportDTO>> violations =
                Validation.buildDefaultValidatorFactory()
                        .getValidator()
                        .validate(data);
        if (!violations.isEmpty()) {
            ErrorRecorder.addError(violations.iterator().next().getMessage());
            return false;
        }

        // 业务校验（如用户名唯一性）
        if (userService.getOne(new QueryWrapper<Account>()
                .eq("username", data.getUsername())) != null) {
            ErrorRecorder.addError("用户名已存在");
            return false;
        }

        return true;
    }
}
