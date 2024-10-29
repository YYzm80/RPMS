package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.dto.common.Static;
import com.example.entity.vo.response.HomeStatic;
import com.example.entity.vo.response.StaticVO;

import java.util.List;

public interface StaticService extends IService<Static> {

    HomeStatic getHomeStatic();
    StaticVO getStaticByMid(Integer mid);
    String doStatic(Integer mid);
}
