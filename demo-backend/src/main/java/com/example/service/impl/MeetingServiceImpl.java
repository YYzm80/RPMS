package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.dto.meeting.Meeting;
import com.example.entity.dto.meeting.Recruitment;
import com.example.entity.vo.response.MeetingVO;
import com.example.mapper.MeetingMapper;
import com.example.mapper.RecruitmentMapper;
import com.example.service.MeetingService;
import com.example.util.Const;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class MeetingServiceImpl extends ServiceImpl<MeetingMapper, Meeting> implements MeetingService {

    @Resource
    private MeetingMapper mapper;

    @Resource
    private RecruitmentMapper recruitmentMapper;

    @Override
    public List<MeetingVO> getAllMeeting() {
        List<Meeting> list = mapper.selectList(null);
        List<MeetingVO> vos = new ArrayList<>();
        list.forEach(l -> l.asViewObject(MeetingVO.class, vo -> {
            voWhenStateNot0(l, vo);
            vos.add(vo);
        }));
        return vos;
    }

    @Override
    public MeetingVO getMeetingByMid(Integer mid) {
        Meeting meeting = mapper.selectById(mid);
        return meeting.asViewObject(MeetingVO.class, v -> voWhenStateNot0(meeting, v));
    }

    @Override
    public String addMeeting(MultipartFile file, Meeting meeting) {
        if (file != null && !file.isEmpty()) {
            try {
                String fileName = System.currentTimeMillis() + file.getOriginalFilename();
                Path uploadFilePath = Paths.get(Const.UPLOAD_PATH, fileName);
                Files.createDirectories(uploadFilePath.getParent());
                file.transferTo(uploadFilePath.toFile());
                meeting.setImg(fileName);
            } catch (IOException e) {
                e.printStackTrace();
                return "上传宣传图失败，请稍后再试";
            }
        }
        if (new Date().before(meeting.getStartTime())) {
            meeting.setState("0");
        } else {
            return "新增双选会信息失败，开始时间不能早于当前时间";
        }
        return mapper.insert(meeting) > 0 ? null : "新增双选会信息失败，请稍后再试";
    }

    @Override
    public String updateMeeting(MultipartFile file, Meeting meeting) {
        if (file != null && !file.isEmpty()) {
            try {
                String fileName = System.currentTimeMillis() + file.getOriginalFilename();
                Path uploadFilePath = Paths.get(Const.UPLOAD_PATH, fileName);
                Files.createDirectories(uploadFilePath.getParent());
                if (mapper.selectById(meeting.getMid()) != null) {
                    String oldFileName = mapper.selectById(meeting.getMid()).getImg();
                    Path oldFilePath = Paths.get(Const.UPLOAD_PATH, oldFileName);
                    if (oldFileName != null && !oldFileName.isEmpty()) {
                        Files.deleteIfExists(oldFilePath);
                    }
                }
                file.transferTo(uploadFilePath.toFile());
                meeting.setImg(fileName);
            } catch (IOException e) {
                e.printStackTrace();
                return "上传宣传图失败，请稍后再试";
            }
        }
        Date date = new Date();
        if (date.before(meeting.getStartTime())) {
            meeting.setState("0");
        } else if (date.after(meeting.getStartTime()) && date.before(meeting.getEndTime())) {
            meeting.setState("1");
        } else if (date.after(meeting.getEndTime())) {
            meeting.setState("2");
        }
        return mapper.updateById(meeting) > 0 ? null : "更新双选会信息失败，请稍后再试";
    }

    @Override
    public void updateMeetingState() {
        List<Meeting> list = mapper.selectList(null);
        Date date = new Date();
        list.forEach(l -> {
            if (date.before(l.getStartTime())) {
                l.setState("0");
            } else if (date.after(l.getStartTime()) && date.before(l.getEndTime())) {
                l.setState("1");
            } else if (date.after(l.getEndTime())) {
                l.setState("2");
            }
            mapper.updateById(l);
        });
    }

    @Override
    public String deleteMeetingByMid(Integer mid) {
        String FileName = mapper.selectById(mid).getImg();
        Path FilePath = Paths.get(Const.UPLOAD_PATH, FileName);
        if (!FileName.isEmpty()) {
            try {
                Files.deleteIfExists(FilePath);
            } catch (IOException e) {
                e.printStackTrace();
                return "删除双选会宣传图失败，请稍后再试";
            }
        }
        return mapper.deleteById(mid) > 0 ? null : "删除双选会信息失败，请稍后再试";
    }

    private void voWhenStateNot0(Meeting meeting, MeetingVO vo) {
        if (!meeting.getState().equals("0")) {
            if (recruitmentMapper.selectOne(new QueryWrapper<Recruitment>()
                    .eq("mid", meeting.getMid()), false) != null) {
                vo.setCompanyCount(Math.toIntExact(
                        recruitmentMapper.selectCount(new QueryWrapper<Recruitment>()
                                .eq("mid", meeting.getMid()).select("DISTINCT cid"))));
                vo.setRecruitmentCount(Math.toIntExact(
                        recruitmentMapper.selectCount(new QueryWrapper<Recruitment>()
                                .eq("mid", meeting.getMid()))));
            } else {
                vo.setCompanyCount(0);
                vo.setRecruitmentCount(0);
            }

        }
    }
}
