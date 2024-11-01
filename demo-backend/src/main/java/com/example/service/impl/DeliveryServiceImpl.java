package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.dto.interview.Delivery;
import com.example.entity.dto.interview.Interview;
import com.example.entity.vo.response.DeliveryVO;
import com.example.mapper.*;
import com.example.service.DeliveryService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DeliveryServiceImpl extends ServiceImpl<DeliveryMapper, Delivery> implements DeliveryService {
    @Resource
    private DeliveryMapper mapper;

    @Resource
    private AccountMapper accountMapper;

    @Resource
    private RecruitmentMapper recruitmentMapper;

    @Resource
    private ResumeMapper resumeMapper;

    @Resource
    private InterviewMapper interviewMapper;

    @Override
    public List<DeliveryVO> getAllByUid(Integer uid) {
        List<Delivery> list = mapper.selectList(new QueryWrapper<Delivery>().eq("uid", uid));
        if (list != null) return converse(list);
        return null;
    }

    @Override
    public List<DeliveryVO> getAllByCid(Integer cid) {
        List<Delivery> list = mapper.selectDeliveryWithRecruitmentInfoByCid(cid);
        if (list != null) return converse(list);
        return null;
    }

    @Override
    public DeliveryVO getByDid(Integer did) {
        Delivery delivery = mapper.selectById(did);
        return delivery.asViewObject(DeliveryVO.class, v -> {
            v.setUsername(accountMapper.selectById(delivery.getUid()).getName());
            v.setRecruitmentName(recruitmentMapper.selectById(delivery.getRid()).getName());
            v.setResumeFile(resumeMapper.selectById(delivery.getResumeId()).getFile());
        });
    }

    @Override
    public String addDelivery(Delivery delivery) {
        if (mapper.selectOne(new QueryWrapper<Delivery>()
                .eq("uid", delivery.getUid())
                .eq("rid", delivery.getRid())) == null) {
            return mapper.insert(delivery) > 0 ? null : "简历投递失败，请稍后再试";
        } else {
            return "您已投递过该职位，请勿重复投递";
        }

    }

    @Override
    public String updateDelivery(Delivery delivery) {
        return mapper.updateById(delivery) > 0 ? null : "投递简历更换失败，请稍后再试";
    }

    @Override
    public String deleteByDid(Integer did) {
        return mapper.deleteById(did) > 0 ? null : "取消投递失败，请稍后再试";
    }

    private List<DeliveryVO> converse(List<Delivery> list) {
        List<DeliveryVO> vos = new ArrayList<>();
        list.forEach(l -> l.asViewObject(DeliveryVO.class, v -> {
            v.setUsername(accountMapper.selectById(l.getUid()).getName());
            v.setRecruitmentName(recruitmentMapper.selectById(l.getRid()).getName());
            v.setResumeFile(resumeMapper.selectById(l.getResumeId()).getFile());
            v.setInterview(interviewMapper.selectOne(new QueryWrapper<Interview>()
                    .eq("uid", l.getUid())
                    .eq("rid", l.getRid())) != null);
            vos.add(v);
        }));
        return vos;
    }
}
