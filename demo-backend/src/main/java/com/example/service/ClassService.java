package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.dto.school.Clazz;

import java.util.List;

public interface ClassService extends IService<Clazz> {
    List<Clazz> getAllClass();
    List<Clazz> getAllClassByState();
    Clazz getClassByCid(Integer cid);
    String addClass(Clazz clazz);
    String updateClass(Clazz clazz);
    String deleteClass(Integer cid);
}
