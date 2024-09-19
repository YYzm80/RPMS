package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.dto.game.Registration;
import com.example.entity.vo.response.RegistrationVO;

import java.util.List;

public interface RegistrationService extends IService<Registration> {

    List<RegistrationVO> getAllRegistrations();
    List<RegistrationVO> getAllRegistrationsByState(String state);
    List<RegistrationVO> getRegistrationByUid(Integer uid);
    List<RegistrationVO> getRegistrationByGid(Integer gid);
    RegistrationVO getRegistrationByRid(Integer rid);
    String addRegistration(Registration registration);
    String updateRegistration(Registration registration);
    String deleteRegistrationByRid(Integer Rid);
}
