package com.example.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.dto.school.Academy;

import java.util.List;

/**
* @author User
* @description 针对表【academy】的数据库操作Service
* @createDate 2024-06-19 09:05:13
*/
public interface AcademyService extends IService<Academy> {
    List<Academy> getAllAcademy();
    List<Academy> getAcademyByState();
    Academy getAcademyByAid(Integer aid);
    String addAcademy(String name);
    String updateAcademy(Academy academy);
    String deleteAcademyByAid(Integer aid);
}
