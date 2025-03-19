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
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Resource
    private UserService service;

    @Resource
    private UserImportFilter filter;

    @Resource
    private DataService<UserImportDTO> userImportService;

    @GetMapping("/all")
    public RestBean<List<AccountVO>> all() {
        return RestBean.success(service.getUserList());
    }

    @GetMapping("/all-owner")
    public RestBean<List<AccountVO>> allOwner() {
        return RestBean.success(service.getOwnerList());
    }

    @GetMapping("/all-property")
    public RestBean<List<AccountVO>> allProperty() {
        return RestBean.success(service.getPropertyOwnerList());
    }

    @GetMapping("/uid/{uid}")
    public RestBean<AccountVO> get(@PathVariable("uid") Integer uid) {
        return RestBean.success(service.getUserByUid(uid));
    }

    @PostMapping("/add")
    public RestBean<String> add(@RequestBody Account account) {
        String s = service.addUser(account);
        return s == null ? RestBean.success("用户添加成功") : RestBean.failure(400, s);
    }

    @PostMapping("/update")
    public RestBean<String> update(@RequestBody Account account) {
        String s = service.updateUser(account);
        return s == null ? RestBean.success("个人资料更新成功") : RestBean.failure(400, s);
    }

    @PostMapping("/delete")
    @PreAuthorize("hasRole('admin')")
    public RestBean<String> delete(@RequestBody Integer uid) {
        String s = service.deleteUserByUid(uid);
        return s == null ? RestBean.success("用户删除成功") : RestBean.failure(400, s);
    }

    @PostMapping("/change")
    @PreAuthorize("hasRole('admin')")
    public RestBean<String> changeStatus(@RequestBody Long uid) {
        Integer s = service.changeStatus(uid);
        return switch (s) {
            case 1 -> RestBean.success("解封成功");
            case 2 -> RestBean.success("封禁成功");
            default -> RestBean.failure(400, "封禁/解封失败");
        };
    }

    @PostMapping("/import")
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
