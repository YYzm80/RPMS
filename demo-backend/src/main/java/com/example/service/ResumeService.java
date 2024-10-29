package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.dto.common.Resume;
import com.example.entity.vo.response.ResumeVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ResumeService extends IService<Resume> {

    List<ResumeVO> getAllResumesByUid(Integer uid);
    ResumeVO getResumesByRid(Integer rid);
    String addResume(MultipartFile file, Resume resume);
    String deleteResume(Integer rid);
}
