package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.dto.common.Types;

import java.util.List;

public interface TypeService extends IService<Types> {
    List<Types> getTypeList(String type);
    List<Types> getTypeListActive(String type);
    Types getTypeById(Long id);
    String addType(Types type);
    String updateType(Types type);
    String changeTypeStatus(Long id);
    String deleteType(Long id);
}
