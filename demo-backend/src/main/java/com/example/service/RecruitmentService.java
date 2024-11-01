package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.dto.meeting.Recruitment;
import com.example.entity.vo.response.RecruitmentVO;

import java.util.List;

public interface RecruitmentService extends IService<Recruitment> {
    List<RecruitmentVO> getAllRecruitmentByMid(Integer mid);
    List<RecruitmentVO> getAllRecruitmentByCid(Integer cid);
    RecruitmentVO getRecruitmentByRid(Integer rid);
    String addRecruitment(Recruitment recruitment, List<Integer> tidList);
    String updateRecruitment(Recruitment recruitment, List<Integer> tidList);
    String deleteRecruitment(Integer rid);
}
