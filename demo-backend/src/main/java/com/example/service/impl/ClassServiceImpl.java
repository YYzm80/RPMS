package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.dto.school.Clazz;
import com.example.mapper.ClassMapper;
import com.example.service.ClassService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClassServiceImpl extends ServiceImpl<ClassMapper, Clazz> implements ClassService {

    @Resource
    private ClassMapper mapper;

    @Override
    public List<Clazz> getAllClass() {
        return mapper.selectList(null);
    }

    @Override
    public List<Clazz> getAllClassByState() {
        return mapper.selectList(new QueryWrapper<Clazz>().eq("state", "1"));
    }

    @Override
    public Clazz getClassByCid(Integer cid) {
        return mapper.selectOne(new QueryWrapper<Clazz>().eq("cid", cid));
    }

    @Override
    public String addClass(Clazz clazz) {
        return mapper.insert(clazz) > 0 ? null : "新增班级失败，请稍后再试";
    }

    @Override
    public String updateClass(Clazz clazz) {
        return mapper.updateById(clazz) > 0 ? null : "修改班级失败，请稍后再试";
    }

    @Override
    public String deleteClass(Integer cid) {
        return mapper.deleteById(cid) > 0 ? null : "删除班级失败，请稍后再试";
    }
}
