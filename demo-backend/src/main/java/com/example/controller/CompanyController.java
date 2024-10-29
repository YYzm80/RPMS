package com.example.controller;

import com.example.entity.RestBean;
import com.example.entity.dto.common.Company;
import com.example.service.CompanyService;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/company")
public class CompanyController {
    @Resource
    private CompanyService service;

    @GetMapping("/all")
    public RestBean<List<Company>> all() {
        return RestBean.success(service.getAllCompany());
    }

    @GetMapping("/{cid}")
    public RestBean<Company> cid(@PathVariable Integer cid) {
        return RestBean.success(service.getCompanyByCid(cid));
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('admin')")
    public RestBean<String> add(@RequestParam("file") MultipartFile file, Company company) {
        String s = service.addCompany(file, company);
        return s == null ? RestBean.success("注册公司信息成功") : RestBean.failure(400, s);
    }

    @PostMapping("/update")
    @PreAuthorize("hasRole('admin')")
    public RestBean<String> update(@RequestParam(value = "file", required = false) MultipartFile file,
                                   Company company) {
        String s = service.updateCompany(file, company);
        return s == null ? RestBean.success("更新公司信息成功") : RestBean.failure(400, s);
    }

    @PostMapping("/delete")
    @PreAuthorize("hasRole('admin')")
    public RestBean<String> delete(Integer cid) {
        String s = service.deleteCompanyByCid(cid);
        return s == null ? RestBean.success("删除公司信息成功") : RestBean.failure(400, s);
    }
}
