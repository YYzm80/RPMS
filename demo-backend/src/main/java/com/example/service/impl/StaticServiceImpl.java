package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.entity.dto.auth.Account;
import com.example.entity.dto.common.Complaint;
import com.example.entity.dto.stat.PaymentTypeStat;
import com.example.entity.dto.common.Property;
import com.example.entity.dto.common.Repair;
import com.example.entity.vo.response.HomeDataBackendVO;
import com.example.entity.vo.response.HomeDataVO;
import com.example.mapper.*;
import com.example.service.AnnounceService;
import com.example.service.StaticService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
public class StaticServiceImpl implements StaticService {

    @Resource
    private AccountMapper accountMapper;
    @Resource
    private PropertyMapper propertyMapper;
    @Resource
    private AnnounceService announceService;
    @Resource
    private ComplaintMapper complaintMapper;
    @Resource
    private RepairMapper repairMapper;
    @Resource
    private StaticMapper staticMapper;

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

    @Override
    public HomeDataBackendVO getHomeDataBackend(Long handlerId) {
        HomeDataBackendVO vo = new HomeDataBackendVO();

        LocalDate today = LocalDate.now(ZoneId.of("Asia/Shanghai")); // 指定时区

        vo.setNewComplaintsAndRepairsToday(Math.toIntExact(complaintMapper.selectCount(new QueryWrapper<Complaint>()
                .gt("submit_time", today)) + repairMapper.selectCount(new QueryWrapper<Repair>()
                .gt("submit_time", today))));
        vo.setUnsolvedComplaintsAndRepairs(Math.toIntExact(complaintMapper.selectCount(new QueryWrapper<Complaint>()
                .eq("status", "pending")) + repairMapper.selectCount(new QueryWrapper<Repair>()
                .eq("status", "pending"))));
        vo.setSolvingComplaintsAndRepairs(Math.toIntExact(complaintMapper.selectCount(new QueryWrapper<Complaint>()
                .eq("status", "processing")) + repairMapper.selectCount(new QueryWrapper<Repair>()
                .eq("status", "processing"))));
        vo.setMySolvedComplaintsAndRepairs(Math.toIntExact(complaintMapper.selectCount(new QueryWrapper<Complaint>()
                .eq("status", "resolved")
                .eq("handler_id", handlerId)) + repairMapper.selectCount(new QueryWrapper<Repair>()
                .eq("status", "completed")
                .eq("handler_id", handlerId))));
        vo.setDailyWorkStatList(staticMapper.getDailyWorkStat());
        List<PaymentTypeStat> stats = staticMapper.getPaymentTypeStat();
        // 计算百分比
        BigDecimal total = stats.stream()
                .map(PaymentTypeStat::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        stats.forEach(stat ->
                stat.setPercentage(stat.getTotalAmount().divide(total, 4, RoundingMode.HALF_UP))
        );
        vo.setPaymentTypeStatList(stats);
        return vo;
    }
}
