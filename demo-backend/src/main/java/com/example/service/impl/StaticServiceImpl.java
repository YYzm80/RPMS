package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.entity.dto.auth.Account;
import com.example.entity.dto.common.Property;
import com.example.entity.vo.response.HomeDataVO;
import com.example.mapper.AccountMapper;
import com.example.mapper.PropertyMapper;
import com.example.service.AnnounceService;
import com.example.service.StaticService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class StaticServiceImpl implements StaticService {

    @Resource
    private AccountMapper accountMapper;

    @Resource
    private PropertyMapper propertyMapper;

    @Resource
    private AnnounceService announceService;

    @Override
    public HomeDataVO getHomeData() {
        HomeDataVO vo = new HomeDataVO();
        vo.setUserCount(accountMapper.selectCount(new QueryWrapper<Account>().eq("rid", 3)));
        vo.setLastMonthUserCount(vo.getUserCount() - accountMapper.selectCount(new QueryWrapper<Account>()
                .lt("created_at", new Date(System.currentTimeMillis() - 30L * 24 * 60 * 60 * 1000))
                .eq("rid", 3)
        ));
        vo.setEmptyPropertyCount(propertyMapper.selectCount(new QueryWrapper<Property>().eq("status", "vacant")));
        vo.setLastMonthEmptyPropertyCount(vo.getEmptyPropertyCount() - propertyMapper.selectCount(new QueryWrapper<Property>()
                .lt("purchase_date", new Date(System.currentTimeMillis() - 30L * 24 * 60 * 60 * 1000))
                .eq("status", "vacant")));
        vo.setAnnouncements(announceService.getLaststAnnouncementList());
        return vo;
    }
}
