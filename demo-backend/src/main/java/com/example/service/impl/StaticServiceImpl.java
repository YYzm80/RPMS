package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.dto.common.Static;
import com.example.entity.dto.meeting.Meeting;
import com.example.entity.dto.meeting.Recruitment;
import com.example.entity.vo.response.HomeStatic;
import com.example.entity.vo.response.StaticVO;
import com.example.mapper.*;
import com.example.service.StaticService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class StaticServiceImpl extends ServiceImpl<StaticMapper, Static> implements StaticService {
    @Resource
    private StaticMapper mapper;

    @Resource
    private AccountMapper accountMapper;

    @Resource
    private MeetingMapper meetingMapper;

    @Resource
    private CompanyMapper companyMapper;

    @Resource
    private RecruitmentMapper recruitmentMapper;

    @Override
    public HomeStatic getHomeStatic() {
        HomeStatic homeStatic = new HomeStatic();
        homeStatic.setMeetingCount(meetingMapper.selectCount(null));
        homeStatic.setMeetingRunCount(meetingMapper.selectCount(new QueryWrapper<Meeting>()
                .eq("state", '1')));
        homeStatic.setUserCount(accountMapper.selectCount(null));
        homeStatic.setCompanyCount(companyMapper.selectCount(null));
        homeStatic.setPopularCompanies(mapper.getPopularCompany());
        return homeStatic;
    }

    @Override
    public StaticVO getStaticByMid(Integer mid) {
        Static mid1 = mapper.selectOne(new QueryWrapper<Static>().eq("mid", mid));
        if (mid1 == null) return null;
        return mid1.asViewObject(StaticVO.class, v -> v.setMeetingName(meetingMapper.selectById(mid).getName()));
    }

    @Override
    public String doStatic(Integer mid) {
        Static static1 = new Static();
        static1.setMid(mid);
        static1.setStudentNum(mapper.getStudentNum(mid));
        static1.setCompanyNum(mapper.getCompanyNum(mid));
        static1.setRecruitmentNum(Math.toIntExact(recruitmentMapper.selectCount(new QueryWrapper<Recruitment>()
                .eq("mid", mid))));
        static1.setResumeNum(mapper.getResumeNum(mid));
        static1.setInterviewNum(mapper.getInterviewNum(mid));
        static1.setInterviewRate((static1.getInterviewNum() * 1.0) / (static1.getResumeNum() * 1.0) * 100.0);
        static1.setInterviewPassRate((mapper.getInterviewPassNum(mid) * 1.0) / (static1.getInterviewNum() * 1.0) * 100);
        return mapper.insert(static1) > 0 ? null : "统计数据失败，请稍后再试";
    }
}
