package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.dto.common.TagRel;
import com.example.entity.dto.common.Tags;
import com.example.entity.dto.meeting.Recruitment;
import com.example.entity.vo.response.RecruitmentVO;
import com.example.mapper.*;
import com.example.service.RecruitmentService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RecruitmentServiceImpl extends ServiceImpl<RecruitmentMapper, Recruitment> implements RecruitmentService {
    @Resource
    private RecruitmentMapper mapper;

    @Resource
    private CompanyMapper companyMapper;

    @Resource
    private MeetingMapper meetingMapper;

    @Resource
    private TagsMapper tagsMapper;

    @Resource
    private TagRelMapper tagRelMapper;

    @Override
    public List<RecruitmentVO> getAllRecruitmentByMid(Integer mid) {
        List<Recruitment> list = mapper.selectList(new QueryWrapper<Recruitment>().eq("mid", mid));
        if (list != null) return converse(list);
        return null;
    }

    @Override
    public List<RecruitmentVO> getAllRecruitmentByCid(Integer cid) {
        List<Recruitment> list = mapper.selectList(new QueryWrapper<Recruitment>().eq("cid", cid));
        if (list != null) return converse(list);
        return null;
    }

    @Override
    public RecruitmentVO getRecruitmentByRid(Integer rid) {
        return mapper.selectById(rid).asViewObject(RecruitmentVO.class, v -> {
            v.setMeetingName(meetingMapper.selectById(v.getMid()).getName());
            v.setCompanyName(companyMapper.selectById(v.getCid()).getName());
            v.setImg(companyMapper.selectById(v.getCid()).getPhoto());
            List<Tags> tags = new ArrayList<>();
            List<TagRel> relList = tagRelMapper.selectList(new QueryWrapper<TagRel>().eq("rid", rid));
            relList.forEach(rel -> {
                Tags tag = tagsMapper.selectById(rel.getTid());
                tags.add(tag);
            });
            v.setTagsList(tags);
        });
    }

    @Override
    public String addRecruitment(Recruitment recruitment, List<Integer> tidList) {
        String s;
        if (mapper.insert(recruitment) > 0) {
            tagRelMapper.insertBatchSomeColumn(getTagList(recruitment, tidList));
            s = null;
        } else s = "发布岗位失败，请稍后再试";
        return s;
    }

    @Override
    public String updateRecruitment(Recruitment recruitment, List<Integer> tidList) {
        tagRelMapper.delete(new QueryWrapper<TagRel>().eq("rid", recruitment.getRid()));
        tagRelMapper.insertBatchSomeColumn(getTagList(recruitment, tidList));
        return mapper.updateById(recruitment) > 0 ? null : "更新岗位信息失败，请稍后再试";
    }

    @Override
    public String deleteRecruitment(Integer rid) {
        tagRelMapper.delete(new QueryWrapper<TagRel>().eq("rid", rid));
        return mapper.deleteById(rid) > 0 ? null : "删除岗位信息失败，请稍后再试";
    }

    private List<RecruitmentVO> converse(List<Recruitment> list) {
        List<RecruitmentVO> vos = new ArrayList<>();
        list.forEach(l -> l.asViewObject(RecruitmentVO.class, v -> {
            v.setMeetingName(meetingMapper.selectById(l.getMid()).getName());
            v.setCompanyName(companyMapper.selectById(l.getCid()).getName());
            v.setImg(companyMapper.selectById(l.getCid()).getPhoto());
            List<Tags> tags = new ArrayList<>();
            List<TagRel> relList = tagRelMapper.selectList(new QueryWrapper<TagRel>().eq("rid", l.getRid()));
            relList.forEach(rel -> {
                Tags tag = tagsMapper.selectById(rel.getTid());
                tags.add(tag);
            });
            v.setTagsList(tags);
            vos.add(v);
        }));
        return vos;
    }

    private List<TagRel> getTagList(Recruitment recruitment, List<Integer> tidList) {
        List<TagRel> list = new ArrayList<>();
        tidList.forEach(l -> {
            TagRel rel = new TagRel();
            rel.setRid(recruitment.getRid());
            rel.setTid(l);
            list.add(rel);
        });
        return list;
    }
}
