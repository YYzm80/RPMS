package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.dto.common.Announcement;
import com.example.entity.vo.response.AnnouncementVO;
import com.example.mapper.AccountMapper;
import com.example.mapper.AnnouncementMapper;
import com.example.service.AnnounceService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnnounceServiceImpl extends ServiceImpl<AnnouncementMapper, Announcement> implements AnnounceService {

    @Resource
    private AnnouncementMapper mapper;

    @Resource
    private AccountMapper accountMapper;

    @Override
    public List<AnnouncementVO> getAnnouncementList() {
        List<Announcement> list = mapper.selectList(null);
        return list.stream()
                .map(announcement -> convert(announcement, false))
                .toList();
    }

    @Override
    public List<AnnouncementVO> getPublishAnnouncementList() {
        List<Announcement> list = mapper.selectList(new QueryWrapper<Announcement>()
                .eq("status", "published"));
        return list.stream()
                .map(announcement -> convert(announcement, false))
                .toList();
    }

    @Override
    public List<AnnouncementVO> getLaststAnnouncementList() {
        List<Announcement> list = mapper.selectList(new QueryWrapper<Announcement>()
                .eq("status", "published")
                .orderByDesc("publish_time")
                .last("limit 4")
        );
        return list.stream()
                .map(announcement -> convert(announcement, false))
                .toList();
    }

    @Override
    public AnnouncementVO getAnnouncementById(Long id) {
        return convert(mapper.selectById(id), true);
    }

    @Override
    public String addAnnouncement(Announcement announcement) {
        return mapper.insert(announcement) > 0 ? null : "添加公告失败，请稍后再试";
    }

    /**
     * @description: 更新公告
     * @param: [announcement] 待更新的公告
     * @return: java.lang.String null表示成功，其他表示失败原因
     **/
    @Override
    public String updateAnnouncement(Announcement announcement) {
        if (mapper.selectById(announcement).getStatus().equals("deleted")) announcement.setStatus("published");
        return mapper.updateById(announcement) > 0 ? null : "更新公告失败，请稍后再试";
    }

    @Override
    public Integer deleteAnnouncement(Long id) {
        if (mapper.selectById(id).getStatus().equals("deleted")) {
            return mapper.deleteById(id) > 0 ? 1 : 3;
        } else {
            Announcement announcement = new Announcement();
            announcement.setAid(id);
            announcement.setStatus("deleted");
            return mapper.updateById(announcement) > 0 ? 2 : 4;
        }
    }

    private AnnouncementVO convert(Announcement announcement, boolean isForm) {
        return announcement.asViewObject(AnnouncementVO.class, v -> {
            if (!isForm) {
                v.setStatusDesc(announcement.getStatus().equals("published") ? "已发布" : "已删除");
                v.setPublisherName(accountMapper.selectById(announcement.getPublisherId()).getRealName());
            }
        });
    }

}
