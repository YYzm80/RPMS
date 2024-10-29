package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.dto.common.Company;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CompanyService extends IService<Company> {
    List<Company> getAllCompany();
    Company getCompanyByCid(Integer cid);
    String addCompany(MultipartFile file, Company company);
    String updateCompany(MultipartFile file, Company company);
    String deleteCompanyByCid(Integer cid);
}
