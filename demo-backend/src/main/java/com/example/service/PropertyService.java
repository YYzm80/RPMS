package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.dto.common.Property;
import com.example.entity.dto.in.PropertyImportDTO;
import com.example.entity.vo.response.PropertyVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PropertyService extends IService<Property> {
    List<PropertyVO> getPropertyList();
    PropertyVO getPropertyVO(Long id);
    String addProperty(MultipartFile file, Property property);
    String updateProperty(MultipartFile file, Property property);
    String deleteProperty(Long id);
}
