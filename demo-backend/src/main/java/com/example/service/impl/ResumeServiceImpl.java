package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.dto.common.Resume;
import com.example.entity.vo.response.ResumeVO;
import com.example.mapper.AccountMapper;
import com.example.mapper.ResumeMapper;
import com.example.service.ResumeService;
import com.example.util.Const;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class ResumeServiceImpl extends ServiceImpl<ResumeMapper, Resume> implements ResumeService {
    @Resource
    private ResumeMapper mapper;

    @Resource
    private AccountMapper accountMapper;

    @Override
    public List<ResumeVO> getAllResumesByUid(Integer uid) {
        List<Resume> list = mapper.selectList(new QueryWrapper<Resume>().eq("uid", uid));
        List<ResumeVO> vos = new ArrayList<>();
        list.forEach(r -> vos.add(r.asViewObject(ResumeVO.class, vo ->
                vo.setUsername(accountMapper.selectById(r.getUid()).getUsername()))));
        return vos;
    }

    @Override
    public ResumeVO getResumesByRid(Integer rid) {
        return mapper.selectById(rid).asViewObject(ResumeVO.class, vo ->
                vo.setUsername(accountMapper.selectById(mapper.selectById(rid).getUid()).getUsername()));
    }

    @Override
    public String addResume(MultipartFile file, Resume resume) {
        if (file != null && !file.isEmpty()) {
            try {
                String fileName = System.currentTimeMillis() + file.getOriginalFilename();
                Path uploadFilePath = Paths.get(Const.UPLOAD_PATH, fileName);
                Files.createDirectories(uploadFilePath.getParent());
                //
                if (mapper.selectCount(new QueryWrapper<Resume>().eq("uid", resume.getUid())) >= 5) {
                    return "您当前已上传的简历文件数量超过了系统规定的上限（5份），请删除后再上传新简历";
                }
                file.transferTo(uploadFilePath.toFile());
                resume.setName(file.getOriginalFilename());
                resume.setFile(fileName);
            } catch (IOException e) {
                e.printStackTrace();
                return "上传简历失败，请稍后再试";
            }
        }
        return mapper.insert(resume) > 0 ? null : "上传简历失败，请稍后再试";
    }

    @Override
    public String deleteResume(Integer rid) {
        String FileName = mapper.selectById(rid).getFile();
        Path FilePath = Paths.get(Const.UPLOAD_PATH, FileName);
        if (!FileName.isEmpty()) {
            try {
                Files.deleteIfExists(FilePath);
            } catch (IOException e) {
                e.printStackTrace();
                return "删除简历失败，请稍后再试";
            }
        }
        return mapper.deleteById(rid) > 0 ? null : "删除简历失败，请稍后再试";
    }
}
