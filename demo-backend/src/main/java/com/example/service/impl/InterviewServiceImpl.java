package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.dto.interview.Interview;
import com.example.entity.vo.response.InterviewVO;
import com.example.mapper.AccountMapper;
import com.example.mapper.InterviewMapper;
import com.example.mapper.RecruitmentMapper;
import com.example.service.InterviewService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class InterviewServiceImpl extends ServiceImpl<InterviewMapper, Interview> implements InterviewService {
    @Resource
    private InterviewMapper mapper;

    @Resource
    private AccountMapper accountMapper;

    @Resource
    private RecruitmentMapper recruitmentMapper;

    @Override
    public List<InterviewVO> getAllByUid(Integer uid) {
        List<Interview> list = mapper.selectList(new QueryWrapper<Interview>().eq("uid", uid));
        if (list != null) return converse(list);
        return null;
    }

    @Override
    public List<InterviewVO> getAllByCid(Integer cid) {
        List<Interview> list = mapper.selectInterviewWithRecruitmentInfoByCid(cid);
        if (list != null) return converse(list);
        return null;
    }

    @Override
    public InterviewVO getByIid(Integer iid) {
        Interview interview = mapper.selectById(iid);
        return interview.asViewObject(InterviewVO.class, v -> {
            v.setUsername(accountMapper.selectById(interview.getUid()).getName());
            v.setRecruitmentName(recruitmentMapper.selectById(interview.getRid()).getName());
        });
    }

    @Override
    public String addInterview(Interview interview) {
        if (mapper.selectOne(new QueryWrapper<Interview>()
                .eq("uid", interview.getUid())
                .eq("rid", interview.getRid())) != null) {
            return "请勿重复发出面试申请";
        } else return mapper.insert(interview) > 0 ? null : "发出面试失败，请稍后再试";
    }

    @Override
    public String updateInterview(Interview interview) {
        return mapper.updateById(interview) > 0 ? null : "面试状态更新失败，请稍后再试";
    }

    @Override
    public String deleteInterviewByIid(Integer iid) {
        return mapper.deleteById(iid) > 0 ? null : "取消面试失败，请稍后再试";
    }

    private List<InterviewVO> converse(List<Interview> list) {
        List<InterviewVO> vos = new ArrayList<>();
        list.forEach(l -> l.asViewObject(InterviewVO.class, v -> {
            v.setUsername(accountMapper.selectById(l.getUid()).getName());
            v.setRecruitmentName(recruitmentMapper.selectById(l.getRid()).getName());
            vos.add(v);
        }));
        return vos;
    }
}
