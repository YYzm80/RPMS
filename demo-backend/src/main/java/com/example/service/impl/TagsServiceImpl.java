package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.dto.common.Tags;
import com.example.mapper.TagsMapper;
import com.example.service.TagsService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagsServiceImpl extends ServiceImpl<TagsMapper, Tags> implements TagsService {

    @Resource
    TagsMapper mapper;

    @Override
    public List<Tags> getAllTags() {
        return mapper.selectList(null);
    }

    @Override
    public Tags getTagByTid(Integer tid) {
        return mapper.selectById(tid);
    }

    @Override
    public String addTag(Tags tags) {
        if (mapper.selectOne(new QueryWrapper<Tags>().eq("name", tags.getName())) != null)
            return "新增标签失败，标签已存在";
        return mapper.insert(tags) > 0 ? null : "新增标签失败，请稍后再试";
    }

    @Override
    public String updateTag(Tags tags) {
        return mapper.updateById(tags) > 0 ? null : "更新标签失败，请稍后再试";
    }

    @Override
    public String deleteTagByTid(Integer tid) {
        return mapper.deleteById(tid) > 0 ? null : "删除标签失败，请稍后再试";
    }
}
