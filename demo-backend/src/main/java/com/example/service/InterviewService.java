package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.dto.interview.Interview;
import com.example.entity.vo.response.InterviewVO;

import java.util.List;

public interface InterviewService extends IService<Interview> {
    List<InterviewVO> getAllByUid(Integer uid);
    List<InterviewVO> getAllByCid(Integer cid);
    InterviewVO getByIid(Integer iid);
    String addInterview(Interview interview);
    String updateInterview(Interview interview);
    String deleteInterviewByIid(Integer iid);
}
