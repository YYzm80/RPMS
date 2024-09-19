package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.dto.game.Games;
import com.example.entity.dto.game.Registration;
import com.example.entity.vo.response.RegistrationVO;
import com.example.mapper.AccountMapper;
import com.example.mapper.GamesMapper;
import com.example.mapper.RegistrationMapper;
import com.example.service.RegistrationService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RegistrationServiceImpl extends ServiceImpl<RegistrationMapper, Registration> implements RegistrationService {

    @Resource
    private RegistrationMapper mapper;

    @Resource
    private AccountMapper accountMapper;

    @Resource
    private GamesMapper gamesMapper;

    @Override
    public List<RegistrationVO> getAllRegistrations() {
        List<Registration> registrations = mapper.selectList(null);
        return this.converse(registrations);
    }

    @Override
    public List<RegistrationVO> getAllRegistrationsByState(String state) {
        List<Registration> registrations = mapper.selectList(new QueryWrapper<Registration>().eq("state", state));
        return this.converse(registrations);
    }

    @Override
    public List<RegistrationVO> getRegistrationByUid(Integer uid) {
        List<Registration> registrations = mapper.selectList(new QueryWrapper<Registration>().eq("uid", uid));
        return this.converse(registrations);
    }

    @Override
    public List<RegistrationVO> getRegistrationByGid(Integer gid) {
        List<Registration> registrations = mapper.selectList(new QueryWrapper<Registration>().eq("gid", gid));
        return this.converse(registrations);
    }

    @Override
    public RegistrationVO getRegistrationByRid(Integer rid) {
        Registration registration = mapper.selectById(rid);
        return mapper.selectById(rid).asViewObject(RegistrationVO.class, v -> {
            v.setName(accountMapper.selectById(registration.getUid()).getName());
            v.setGName(gamesMapper.selectById(registration.getGid()).getName());
        });
    }

    @Override
    public String addRegistration(Registration registration) {
        Long count = mapper.selectCount(new QueryWrapper<Registration>()
                .eq("gid", registration.getGid())
                .eq("role", registration.getRole()));
        Games gameInfo = gamesMapper.selectById(registration.getGid());

        String errorMessage = (registration.getRole().equals("athlete") && count >= gameInfo.getPlayerNum()) ?
                "报名失败，该比赛运动员人数已满" : (registration.getRole().equals("referee") && count >= gameInfo.getRefereeNum()) ?
                        "报名失败，该比赛裁判人数已满" : null;
        if (errorMessage != null) return errorMessage;

        if (mapper.selectOne(new QueryWrapper<Registration>()
                .eq("uid", registration.getUid())
                .eq("gid", registration.getGid())) != null) return "报名失败，您已报名该比赛";
        return mapper.insert(registration) > 0 ? null : "报名失败，请稍后再试";
    }

    @Override
    public String updateRegistration(Registration registration) {
        String state = registration.getState();
        switch (state) {
            case "1" -> registration.setState("1");
            case "2" -> registration.setState("2");
        }
        return mapper.updateById(registration) > 0 ? state : "审核报名信息失败，请稍后再试";
    }

    @Override
    public String deleteRegistrationByRid(Integer rid) {
        return mapper.deleteById(rid) > 0 ? null : "取消报名失败，请稍后再试";
    }

    private List<RegistrationVO> converse(List<Registration> registrations) {
        List<RegistrationVO> registrationVOS = new ArrayList<>();
        for (Registration registration : registrations) {
            registrationVOS.add(registration.asViewObject(RegistrationVO.class, v -> {
                v.setName(accountMapper.selectById(registration.getUid()).getName());
                v.setGName(gamesMapper.selectById(registration.getGid()).getName());
            }));
        }
        return registrationVOS;
    }
}
