package com.example.entity.dto.in;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.example.entity.BaseData;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.Date;

@Data
public class UserImportDTO implements BaseData {
    @ExcelProperty(index = 0, value = "用户名")
    @NotBlank(message = "用户名不能为空")
    private String username;

    @ExcelProperty(index = 1, value = "真实姓名")
    @NotBlank(message = "真实姓名不能为空")
    private String realName;

    @ExcelProperty(index = 2, value = "性别")
    private String gender;

    @ExcelProperty(index = 3, value = "手机号")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式错误")
    private String phone;

    @ExcelProperty(index = 4, value = "邮箱")
    @Pattern(regexp = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$", message = "邮箱格式错误")
    private String address;

    @ExcelProperty(index = 5, value = "角色")
    private Long rid;

    @ExcelProperty(index = 6, value = "职位")
    private String position;

    @ExcelProperty(index = 7, value = "入职时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat("yyyy-MM-dd")
    private Date hireDate;


}
