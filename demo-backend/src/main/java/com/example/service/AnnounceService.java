package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.dto.common.Announcement;
import com.example.entity.vo.response.AnnouncementVO;

import java.util.List;

public interface AnnounceService extends IService<Announcement> {
    List<AnnouncementVO> getAnnouncementList();
    List<AnnouncementVO> getPublishAnnouncementList();
    List<AnnouncementVO> getLaststAnnouncementList();
    AnnouncementVO getAnnouncementById(Long id);
    String addAnnouncement(Announcement announcement);
    String updateAnnouncement(Announcement announcement);
    Integer deleteAnnouncement(Long id);
}
