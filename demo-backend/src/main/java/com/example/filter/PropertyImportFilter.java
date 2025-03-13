package com.example.filter;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.entity.dto.auth.Account;
import com.example.entity.dto.common.Property;
import com.example.entity.dto.in.PropertyImportDTO;
import com.example.mapper.AccountMapper;
import com.example.service.PropertyService;
import com.example.util.ErrorRecorder;
import jakarta.annotation.Resource;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component("propertyImportFilter")
public class PropertyImportFilter implements ExcelDataFilter<PropertyImportDTO>{

    private final PropertyService propertyService;
    @Resource
    private AccountMapper accountMapper;

    @Autowired
    public PropertyImportFilter(PropertyService propertyService) {
        this.propertyService = propertyService;
    }

    @Override
    public boolean filter(PropertyImportDTO data, Integer rowIndex) {
        // 基础校验（JSR303）
        Set<ConstraintViolation<PropertyImportDTO>> violations =
                Validation.buildDefaultValidatorFactory()
                        .getValidator()
                        .validate(data);
        if (!violations.isEmpty()) {
            ErrorRecorder.addError(violations.iterator().next().getMessage());
            return false;
        }

        // 业务校验
        if (propertyService.getOne(new QueryWrapper<Property>()
                .eq("building_number", data.getBuildingNumber())
                .and(q -> q.eq("room_number", data.getRoomNumber()))) != null) {
            ErrorRecorder.addError("该房屋号和楼栋已存在");
            return false;
        }
        if (data.getOwnerName() != null && accountMapper.selectOne(new QueryWrapper<Account>()
                .eq("real_name", data.getOwnerName())) == null) {
            ErrorRecorder.addError("该业主不存在");
            return false;
        }

        return true;
    }
}
