package com.example.service;

import com.example.entity.dto.score.ScoreDetail;
import com.example.entity.vo.response.ScoreDetailVO;
import com.example.entity.vo.response.ScoreListVO;

import java.util.List;

public interface ScoreService {

    List<ScoreDetailVO> getAllScoreBySid(Integer sid);
    List<ScoreDetailVO> getAllScoreBySidSort(Integer sid);
    List<ScoreDetailVO> getAllScoreByUid(Integer uid);
    List<ScoreListVO> getAllScoreList();
    ScoreDetailVO getScoreById(Integer id);
    ScoreListVO getScoreListByGid(Integer gid);
    ScoreListVO getScoreListBySid(Integer sid);
    String initScoreList(Integer gid);
    String publishScore(Integer sid);
    String updateScore(ScoreDetail scoreDetail);
    String deleteScoreById(Integer id);
}
