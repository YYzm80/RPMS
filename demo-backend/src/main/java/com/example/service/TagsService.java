package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.dto.common.Tags;

import java.util.List;

public interface TagsService extends IService<Tags> {
    List<Tags> getAllTags();
    Tags getTagByTid(Integer tid);
    String addTag(Tags tags);
    String updateTag(Tags tags);
    String deleteTagByTid(Integer tid);
}
