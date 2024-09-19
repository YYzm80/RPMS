package com.example.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.dto.school.Academy;
import com.example.mapper.AcademyMapper;
import com.example.service.AcademyService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author User
* @description 针对表【academy】的数据库操作Service实现
* @createDate 2024-06-19 09:05:13
*/
@Service
public class AcademyServiceImpl extends ServiceImpl<AcademyMapper, Academy> implements AcademyService {

    @Resource
    private AcademyMapper mapper;

    @Override
    public List<Academy> getAllAcademy() {
        return mapper.getAllAcademy();
    }

    @Override
    public List<Academy> getAcademyByState() {
        return mapper.getAllAcademyByState();
    }

    @Override
    public Academy getAcademyByAid(Integer aid) {
        return mapper.getAcademyByAId(aid);
    }

    @Override
    public String addAcademy(String name) {
        return mapper.addAcademy(name) > 0 ? null : "添加学院失败，请稍后再试";
    }

    @Override
    public String updateAcademy(Academy academy) {
        return mapper.updateAcademy(academy) > 0 ? null : "更新学院信息失败，请稍后再试";
    }

    @Override
    public String deleteAcademyByAid(Integer aid) {
        return mapper.deleteAcademyByAid(aid) > 0 ? null : "删除学院失败，请稍后再试";

    }
}




