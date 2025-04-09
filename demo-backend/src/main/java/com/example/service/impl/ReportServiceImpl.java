package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.dto.auth.Account;
import com.example.entity.dto.common.*;
import com.example.entity.dto.stat.*;
import com.example.entity.vo.response.MonthlyStatVO;
import com.example.mapper.*;
import com.example.service.ReportService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class ReportServiceImpl extends ServiceImpl<ReportMapper, Report> implements ReportService {
    @Resource
    private ReportMapper mapper;
    @Resource
    private StaticMapper staticMapper;
    @Resource
    private AccountMapper accountMapper;
    @Resource
    private PropertyMapper propertyMapper;
    @Resource
    private ComplaintMapper complaintMapper;
    @Resource
    private RepairMapper repairMapper;
    @Resource
    private AnnouncementMapper announcementMapper;

    @Override
    public String generateReport(Date month) {
        SimpleDateFormat mm = new SimpleDateFormat("yyyy-MM");
        SimpleDateFormat std = new SimpleDateFormat("yyyy-MM-dd");

        if (mapper.selectOne(new QueryWrapper<Report>().eq("month", mm.format(month))) != null) {
            return "该月已生成过月报";
        }

        Report report = new Report();
        // 获取月报内容
        MonthlyStat content = getContent(month);

        report.setMonth(mm.format(month));
        report.setContent(content);
        Date date = new Date();
        try {
            report.setGenerateTime(std.parse(std.format(date)));
        } catch (ParseException e) {
            log.error("日期格式化错误", e);
        }

        return mapper.insert(report) > 0 ? null : "生成月报失败";
    }

    @Override
    public List<MonthlyStatVO> getReportList(Date month) {
        List<Report> list;
        if (month != null) {
            list = mapper.selectList(new QueryWrapper<Report>().eq("month", month));
        } else {
            list = mapper.selectList(null);
        }

        return list.stream().map(report -> report.getContent().asViewObject(MonthlyStatVO.class, vo -> {
            vo.setMonth(report.getMonth());
            vo.setGenerateTime(report.getGenerateTime());
        })).toList();
    }

    @Override
    public MonthlyStatVO getReport(Long id) {
        Report report = mapper.selectById(id);
        return report.getContent().asViewObject(MonthlyStatVO.class, vo -> {
            vo.setMonth(report.getMonth());
            vo.setGenerateTime(report.getGenerateTime());
        });
    }

    @Override
    public String deleteReport(Long id) {
        return mapper.deleteById(id) > 0 ? null : "删除月报失败";
    }

    /**
     * 获取月报内容
     *
     * @param month 指定月份
     * @return 月报内容
     */
    private MonthlyStat getContent(Date month) {
        // 初始化月报内容对象
        MonthlyStat content = new MonthlyStat();
        DecimalFormat df = new DecimalFormat("#.00");

        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(month);
        rightNow.add(Calendar.MONTH, 1);
        Date nextMonth = rightNow.getTime();

        SimpleDateFormat mm = new SimpleDateFormat("yyyy-MM");
        try {
            nextMonth = mm.parse(mm.format(nextMonth));
        } catch (ParseException e) {
            log.error("日期格式化错误", e);
        }
        // 统计本月社区基本信息
        MonthlyBasicCommunityStat basicCommunityStat = new MonthlyBasicCommunityStat();
        basicCommunityStat.setLivingCount(Math.toIntExact(accountMapper.selectCount(new QueryWrapper<Account>()
                .lt("created_at", nextMonth)
                .eq("rid", 3))));
        basicCommunityStat.setEmptyPropertyRate(Double.valueOf(df.format(1.0 *
                propertyMapper.selectCount(new QueryWrapper<Property>().lt("created_at", nextMonth).eq("status", "vacant"))
                / propertyMapper.selectCount(new QueryWrapper<Property>().lt("created_at", nextMonth)))));
        basicCommunityStat.setComplaintCount(Math.toIntExact(complaintMapper.selectCount(new QueryWrapper<Complaint>()
                .gt("submit_time", month)
                .lt("submit_time", nextMonth))));
        basicCommunityStat.setRepairCount(Math.toIntExact(repairMapper.selectCount(new QueryWrapper<Repair>()
                .gt("submit_time", month)
                .lt("submit_time", nextMonth))));
        content.setBasicCommunityStat(basicCommunityStat);

        // 统计本月社区工作状态
        MonthlyManagerWorkStat managerWorkStat = new MonthlyManagerWorkStat();
        managerWorkStat.setManagerCount(Math.toIntExact(accountMapper.selectCount(new QueryWrapper<Account>()
                .lt("hire_date", nextMonth).eq("rid", 2))));
        managerWorkStat.setResolvedComplaintsCount(Math.toIntExact(complaintMapper.selectCount(new QueryWrapper<Complaint>()
                .gt("submit_time", month)
                .lt("submit_time", nextMonth)
                .eq("status", "resolved"))));
        managerWorkStat.setComplaintResolvedRate(Double.valueOf(df.format(1.0 *
                managerWorkStat.getResolvedComplaintsCount() / basicCommunityStat.getComplaintCount())));
        managerWorkStat.setSolvedRepairsCount(Math.toIntExact(repairMapper.selectCount(new QueryWrapper<Repair>()
                .gt("submit_time", month)
                .lt("submit_time", nextMonth)
                .eq("status", "completed"))));
        managerWorkStat.setRepairSolvedRate(Double.valueOf(df.format(1.0 *
                managerWorkStat.getSolvedRepairsCount() / basicCommunityStat.getRepairCount())));
        managerWorkStat.setPublishedAnnouncementCount(Math.toIntExact(announcementMapper.selectCount(new QueryWrapper<Announcement>()
                .gt("publish_time", month)
                .lt("publish_time", nextMonth)
                .eq("status", "published"))));
        content.setManagerWorkStat(managerWorkStat);

        // 统计本月收入情况
        MonthlyIncomeState incomeState = new MonthlyIncomeState();
        incomeState.setPaymentTypeStatList(staticMapper.getPaymentTypeMonthStat(month));
        incomeState.setTotal(incomeState.getPaymentTypeStatList().stream().map(PaymentTypeStat::getTotalAmount).reduce(BigDecimal::add).orElse(BigDecimal.ZERO));
        content.setIncomeState(incomeState);

        // 返回月报内容
        return content;
    }
}
