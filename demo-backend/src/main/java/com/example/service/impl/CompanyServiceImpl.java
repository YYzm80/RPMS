package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.dto.auth.Account;
import com.example.entity.dto.common.Company;
import com.example.mapper.AccountMapper;
import com.example.mapper.CompanyMapper;
import com.example.service.CompanyService;
import com.example.util.Const;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class CompanyServiceImpl extends ServiceImpl<CompanyMapper, Company> implements CompanyService {

    @Resource
    private CompanyMapper mapper;

    @Resource
    private AccountMapper accountMapper;

    @Override
    public List<Company> getAllCompany() {
        return mapper.selectList(null);
    }

    @Override
    public Company getCompanyByCid(Integer cid) {
        return mapper.selectById(cid);
    }

    @Override
    public String addCompany(MultipartFile file, Company company) {
        if (uploadFile(file, company)) {
            return mapper.insert(company) > 0 ? null : "注册公司信息失败，请稍后再试";
        } else {
            return "上传图片失败，请稍后再试";
        }
    }

    @Override
    public String updateCompany(MultipartFile file, Company company) {
        if (uploadFile(file, company)) {
            return mapper.updateById(company) > 0 ? null : "更新公司信息失败，请稍后再试";
        } else {
            return "上传图片失败，请稍后再试";
        }
    }

    @Override
    public String deleteCompanyByCid(Integer cid) {
        if (accountMapper.selectOne(new QueryWrapper<Account>().eq("cid", cid)) != null) {
            return "该公司下存在账号，暂时无法删除";
        }
        String FileName = mapper.selectById(cid).getPhoto();
        Path FilePath = Paths.get(Const.UPLOAD_PATH, FileName);
        if (!FileName.isEmpty()) {
            try {
                Files.deleteIfExists(FilePath);
            } catch (IOException e) {
                e.printStackTrace();
                return "删除公司信息失败，请稍后再试";
            }
        }
        return mapper.deleteById(cid) > 0 ? null : "删除公司信息失败，请稍后再试";
    }

    private boolean uploadFile(MultipartFile file, Company company) {
        if (file != null && !file.isEmpty()) {
            try {
                String fileName = System.currentTimeMillis() + file.getOriginalFilename();
                Path uploadFilePath = Paths.get(Const.UPLOAD_PATH, fileName);
                Files.createDirectories(uploadFilePath.getParent());
                if (mapper.selectById(company.getCid()) != null) {
                    String oldFileName = mapper.selectById(company.getCid()).getPhoto();
                    Path oldFilePath = Paths.get(Const.UPLOAD_PATH, oldFileName);
                    if (oldFileName != null && !oldFileName.isEmpty()) {
                        Files.deleteIfExists(oldFilePath);
                    }
                }
                file.transferTo(uploadFilePath.toFile());
                company.setPhoto(fileName);
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }
}
