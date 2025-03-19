package com.example.service;

import com.example.entity.vo.response.HomeDataBackendVO;
import com.example.entity.vo.response.HomeDataVO;

public interface StaticService {
    HomeDataVO getHomeData();
    HomeDataBackendVO getHomeDataBackend(Long handlerId);
}
