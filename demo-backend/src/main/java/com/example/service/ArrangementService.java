package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.dto.game.Arrangement;
import com.example.entity.vo.response.ArrangementVO;

import java.util.List;

public interface ArrangementService extends IService<Arrangement> {

    List<ArrangementVO> getArrangementByGid(Integer gid);
    List<ArrangementVO> getArrangementByUid(Integer uid);
    ArrangementVO getArrangementByAid(Integer aid);
    String doArrangement(Integer gid);
    String updateArrangement(Arrangement arrangement);
    String deleteArrangementByAid(Integer aid);
}
