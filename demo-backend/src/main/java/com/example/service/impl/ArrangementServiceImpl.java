package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.dto.game.Arrangement;
import com.example.entity.dto.game.Registration;
import com.example.entity.vo.response.ArrangementVO;
import com.example.mapper.AccountMapper;
import com.example.mapper.ArrangementMapper;
import com.example.mapper.GamesMapper;
import com.example.mapper.RegistrationMapper;
import com.example.service.ArrangementService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ArrangementServiceImpl extends ServiceImpl<ArrangementMapper, Arrangement> implements ArrangementService {

    @Resource
    private ArrangementMapper mapper;

    @Resource
    private GamesMapper gamesMapper;

    @Resource
    private RegistrationMapper registrationMapper;

    @Resource
    private AccountMapper accountMapper;


    @Override
    public List<ArrangementVO> getArrangementByGid(Integer gid) {
        List<Arrangement> list = mapper.selectList(new QueryWrapper<Arrangement>().eq("gid", gid));
        return this.converse(list);
    }

    @Override
    public List<ArrangementVO> getArrangementByUid(Integer uid) {
        List<Arrangement> list = mapper.selectList(new QueryWrapper<Arrangement>().eq("uid", uid));
        return this.converse(list);
    }

    @Override
    public ArrangementVO getArrangementByAid(Integer aid) {
        return mapper.selectById(aid).asViewObject(ArrangementVO.class, v -> v.setGName(gamesMapper.selectById(aid).getName()));
    }

    @Override
    public String doArrangement(Integer gid) {
        QueryWrapper<Registration> wrapper = new QueryWrapper<Registration>()
                .eq("state", "1")
                .eq("gid", gid);
        List<Registration> players = registrationMapper.selectList(wrapper.eq("role", "athlete"));
        // TODO wrapper需要重置，原因未知
        wrapper = new QueryWrapper<Registration>()
                .eq("state", "1")
                .eq("gid", gid);
        List<Registration> referees = registrationMapper.selectList(wrapper.eq("role", "referee"));
        if (players.isEmpty() || referees.isEmpty()) {
            return "自动安排失败，至少要有一个运动员或裁判员";
        }

        Map<String, List<Registration>> roleRegistrations = new HashMap<>();
        roleRegistrations.put("athlete", players);
        roleRegistrations.put("referee", referees);

        for (Map.Entry<String, List<Registration>> entry : roleRegistrations.entrySet()) {
            for (Registration registration : entry.getValue()) {
                if (!this.insertArrangement(registration.getGid(), registration.getUid(), registration.getRole())) {
                    return "自动安排失败，请稍后再试";
                }
            }
        }
        return null;
    }

    @Override
    public String updateArrangement(Arrangement arrangement) {
        return mapper.updateById(arrangement) > 0 ? null : "更新安排信息失败，请稍后再试";
    }

    @Override
    public String deleteArrangementByAid(Integer aid) {
        return mapper.deleteById(aid) > 0 ? null : "删除安排信息失败，请稍后再试";
    }

    private List<ArrangementVO> converse(List<Arrangement> arrangements) {
        List<ArrangementVO> arrangementVOS = new ArrayList<>();
        for (Arrangement arrangement : arrangements)
            arrangementVOS.add(arrangement.asViewObject(ArrangementVO.class, v -> v.setGName(gamesMapper.selectById(arrangement.getGid()).getName())));
        return arrangementVOS;
    }

    private boolean insertArrangement(Integer gid, Integer uid, String role) {
        Arrangement arrangement = new Arrangement();
        arrangement.setName(accountMapper.selectById(uid).getName());
        arrangement.setGid(gid);
        arrangement.setUid(uid);
        arrangement.setRole(role);
        return mapper.insert(arrangement) > 0;
    }
}
