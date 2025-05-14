package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.dto.common.Types;
import com.example.mapper.TypeMapper;
import com.example.service.TypeService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class TypeServiceImpl extends ServiceImpl<TypeMapper, Types> implements TypeService {
    @Resource
    private TypeMapper mapper;

    @Override
    public List<Types> getTypeList(String type) {
        try {
            return mapper.selectList(new QueryWrapper<Types>().eq("type", type));
        } catch (Exception e) {
            log.error("查询类型失败，请稍后再试", e);
            return null;
        }
    }

    @Override
    public List<Types> getTypeListActive(String type) {
        try {
            return mapper.selectList(new QueryWrapper<Types>().eq("type", type).eq("status", "active"));
        } catch (Exception e) {
            log.error("查询类型失败，请稍后再试", e);
            return null;
        }
    }

    @Override
    public Types getTypeById(Long id) {
        try {
            return mapper.selectById(id);
        } catch (Exception e) {
            log.error("查询类型失败，请稍后再试", e);
            return null;
        }
    }

    @Override
    public String addType(Types types) {
        if (mapper.selectOne(Wrappers.<Types>lambdaQuery()
                .eq(Types::getDescription, types.getDescription()).eq(Types::getType, types.getType())) != null)
            return "当前类型已存在，请更换类型";
        return mapper.insert(types) > 0 ? null : "新增类型失败，请稍后再试";
    }

    @Override
    public String updateType(Types types) {
        if (mapper.selectOne(Wrappers.<Types>lambdaQuery()
                .eq(Types::getDescription, types.getDescription()).eq(Types::getType, types.getType())) != null)
            return "当前类型已存在，请更换类型";
        return mapper.updateById(types) > 0 ? null : "更新类型失败，请稍后再试";
    }

    @Override
    public String changeTypeStatus(Long id) {
        Types types = mapper.selectById(id);
        if (types.getStatus().equals("active"))
            types.setStatus("inactive");
        else
            types.setStatus("active");
        return mapper.updateById(types) > 0 ? null : "更新状态失败，请稍后再试";
    }

    @Override
    public String deleteType(Long id) {
        return mapper.deleteById(id) > 0 ? null : "删除类型失败，请稍后再试";
    }
}
