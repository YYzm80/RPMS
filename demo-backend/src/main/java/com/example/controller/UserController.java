package com.example.controller;

import com.alibaba.excel.EasyExcel;
import com.example.entity.ImportResult;
import com.example.entity.RestBean;
import com.example.entity.dto.auth.Account;
import com.example.entity.dto.in.UserImportDTO;
import com.example.entity.vo.response.AccountVO;
import com.example.filter.UserImportFilter;
import com.example.listener.FilterableExcelReader;
import com.example.service.DataService;
import com.example.service.UserService;
import com.example.util.ErrorRecorder;
import com.example.util.ExcelUtil;
import com.example.util.consts.Const;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import jakarta.annotation.security.RolesAllowed;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Resource
    private UserService service;

    @Resource
    private UserImportFilter filter;

    @Resource
    private DataService<UserImportDTO> userImportService;

    @Operation(summary = "获取所有用户信息")
    @GetMapping("/all")
    public RestBean<List<AccountVO>> all() {
        return RestBean.success(service.getUserList());
    }

    @Operation(summary = "获取所有业主信息")
    @GetMapping("/all-owner")
    public RestBean<List<AccountVO>> allOwner() {
        return RestBean.success(service.getOwnerList());
    }

    @Operation(summary = "获取所有已入住业主信息")
    @GetMapping("/all-property")
    public RestBean<List<AccountVO>> allProperty() {
        return RestBean.success(service.getPropertyOwnerList());
    }

    @Operation(summary = "获取用户信息")
    @GetMapping("/uid/{uid}")
    public RestBean<AccountVO> get(@PathVariable("uid") Integer uid) {
        return RestBean.success(service.getUserByUid(uid));
    }

    @Operation(summary = "下载用户excel表模版")
    @GetMapping("/template")
    public void template(HttpServletResponse response) {
        String fileName = "导入用户模板";
        String sheetName = "导入用户模板";
        List<UserImportDTO> userList = new ArrayList<>();
        userList.add(new UserImportDTO("Alice", "张三", "female", "16300000001", "847064370@qq.com", 3L, null, null));
        userList.add(new UserImportDTO("Bob", "李四", "male", "16300000002", "666666@qq.com", 2L, "保安", new Date()));
        try {
            ExcelUtil.writeExcel(response, userList, fileName, sheetName, UserImportDTO.class);
        } catch (Exception e) {
            log.error("文件模版生成失败", e);
        }
    }

    @Operation(summary = "添加用户")
    @RolesAllowed({Const.ROLE_ADMIN, Const.ROLE_MANAGER})
    @PostMapping("/add")
    public RestBean<String> add(@RequestBody @Valid Account account) {
        String s = service.addUser(account);
        return s == null ? RestBean.success("用户添加成功") : RestBean.failure(400, s);
    }

    @Operation(summary = "修改用户信息")
    @PutMapping("/update-manager")
    @RolesAllowed({Const.ROLE_ADMIN, Const.ROLE_MANAGER})
    public RestBean<String> updateM(@RequestBody @Valid Account account) {
        String s = service.updateUser(account);
        return s == null ? RestBean.success("用户信息修改成功") : RestBean.failure(400, s);
    }

    @Operation(summary = "修改个人资料")
    @PutMapping("/update-personal")
    @RolesAllowed({Const.ROLE_ADMIN, Const.ROLE_MANAGER, Const.ROLE_OWNER})
    public RestBean<String> updateP(@RequestBody @Valid Account account) {
        String s = service.updateUser(account);
        return s == null ? RestBean.success("个人资料更新成功") : RestBean.failure(400, s);
    }

    @Operation(summary = "删除用户")
    @PostMapping("/delete")
    @RolesAllowed({Const.ROLE_ADMIN, Const.ROLE_MANAGER})
    public RestBean<String> delete(@RequestBody Integer uid) {
        String s = service.deleteUserByUid(uid);
        return s == null ? RestBean.success("用户删除成功") : RestBean.failure(400, s);
    }

    @Operation(summary = "封禁/解封用户")
    @PutMapping("/change")
    @RolesAllowed({Const.ROLE_ADMIN, Const.ROLE_MANAGER})
    public RestBean<String> changeStatus(@RequestBody Long uid) {
        Integer s = service.changeStatus(uid);
        return switch (s) {
            case 1 -> RestBean.success("解封成功");
            case 2 -> RestBean.success("封禁成功");
            default -> RestBean.failure(400, "封禁/解封失败");
        };
    }

    @Operation(summary = "导入用户")
    @PostMapping("/import")
    @RolesAllowed({Const.ROLE_ADMIN, Const.ROLE_MANAGER})
    public RestBean<String> importUsers(@RequestParam("file") MultipartFile file) {
        try {
            FilterableExcelReader<UserImportDTO> reader = new FilterableExcelReader<>(filter, userImportService);
            EasyExcel.read(file.getInputStream(), UserImportDTO.class, reader)
                    .sheet()
                    .doRead();
            return checkResult(reader);
        } catch (IOException e) {
            return RestBean.failure(400, "文件读取失败");
        } finally {
            ErrorRecorder.clearErrors(); // 清除错误记录
        }
    }

    private RestBean<String> checkResult(FilterableExcelReader<UserImportDTO> reader) {
        ImportResult result = reader.getResult();
        if (result.getErrors().isEmpty()) {
            return RestBean.success("成功导入" + result.getSuccessCount() + "条用户数据");
        } else {
            return RestBean.failure(400,
                    "成功导入" + result.getSuccessCount() + "条，失败详情：" +
                            String.join("；", result.getErrors()));
        }
    }

}
