package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.dto.auth.Account;
import com.example.entity.dto.common.Property;
import com.example.entity.dto.common.Types;
import com.example.entity.dto.in.PropertyImportDTO;
import com.example.entity.vo.response.PropertyVO;
import com.example.mapper.AccountMapper;
import com.example.mapper.PropertyMapper;
import com.example.mapper.TypeMapper;
import com.example.service.DataService;
import com.example.service.PropertyService;
import com.example.util.consts.Const;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PropertyServiceImpl extends ServiceImpl<PropertyMapper, Property> implements PropertyService, DataService<PropertyImportDTO> {

    @Resource
    private PropertyMapper mapper;
    @Resource
    private AccountMapper accountMapper;
    @Resource
    private TypeMapper typeMapper;

    @Override
    public List<PropertyVO> getPropertyList() {
        List<Property> list = mapper.selectList(null);
        return list.stream()
                .map(property -> convert(property, false))
                .toList();
    }

    @Override
    public PropertyVO getPropertyVO(Long id) {
        return convert(mapper.selectById(id), true);
    }

    /**
     * 添加房产信息
     * @param file 户型图文件
     * @param property 房产信息
     * @return null 表示成功，否则表示失败原因
     */
    @Override
    public String addProperty(MultipartFile file, Property property) {
        if (file != null && !file.isEmpty()) {
            try {
                String fileName = System.currentTimeMillis() + file.getOriginalFilename();
                Path uploadFilePath = Paths.get(Const.UPLOAD_PATH, fileName);
                Files.createDirectories(uploadFilePath.getParent());
                file.transferTo(uploadFilePath.toFile());
                property.setFloorPlan(fileName);
            } catch (IOException e) {
                log.error("上传户型图失败", e);
                return "上传户型图失败，请稍后再试";
            }
        }
        if (property.getUserId() != null) {
            property.setStatus("occupied");
        } else {
            property.setStatus("vacant");
        }

        return mapper.insert(property) > 0 ? null : "添加房产信息失败，请稍后再试";
    }

    @Override
    public String updateProperty(MultipartFile file, Property property) {
        if (file != null && !file.isEmpty()) {
            try {
                String fileName = System.currentTimeMillis() + file.getOriginalFilename();
                Path uploadFilePath = Paths.get(Const.UPLOAD_PATH, fileName);
                Files.createDirectories(uploadFilePath.getParent());
                if (mapper.selectById(property.getPropertyId()) != null) {
                    String oldFileName = mapper.selectById(property.getPropertyId()).getFloorPlan();
                    if (oldFileName != null && !oldFileName.isEmpty()) {
                        Path oldFilePath = Paths.get(Const.UPLOAD_PATH, oldFileName);
                        Files.deleteIfExists(oldFilePath);
                    }
                }
                file.transferTo(uploadFilePath.toFile());
                property.setFloorPlan(fileName);
            } catch (IOException e) {
                log.error("修改户型图失败", e);
                return "修改户型图失败，请稍后再试";
            }
        }
        if (property.getUserId() != null) {
            property.setStatus("occupied");
        } else {
            property.setStatus("vacant");
        }
        return mapper.updateById(property) > 0 ? null : "更新房产信息失败，请稍后再试";
    }

    @Override
    public String deleteProperty(Long id) {
        if (mapper.selectById(id) != null) {
            String fileName = mapper.selectById(id).getFloorPlan();
            if (fileName != null && !fileName.isEmpty()) {
                try {
                    Path filePath = Paths.get(Const.UPLOAD_PATH, fileName);
                    Files.deleteIfExists(filePath);
                } catch (IOException e) {
                    log.error("删除户型图失败", e);
                    return "删除户型图失败，请稍后再试";
               }
            }
        }
        return mapper.deleteById(id) > 0 ? null : "删除房产信息失败，请稍后再试";
    }

    @Override
    public void batchImport(List<PropertyImportDTO> list) {
        List<Property> entities = list.stream()
                .map(dto -> {
                    Property property = new Property();
                    BeanUtils.copyProperties(dto, property);
                    if (dto.getOwnerName() != null) {
                        property.setUserId(accountMapper
                                .selectOne(new QueryWrapper<Account>()
                                        .eq("real_name", dto.getOwnerName())).getUserId());
                        property.setStatus("occupied");
                    } else {
                        property.setStatus("vacant");
                    }
                    property.setTypeId(typeMapper
                            .selectOne(new QueryWrapper<Types>()
                                    .eq("description", dto.getType())
                                    .eq("type", Const.TYPE_PROPERTY)).getTid());
                    property.setCreatedAt(new Date());
                    return property;
                })
                .collect(Collectors.toList());

        mapper.insertBatchSomeColumn(entities);
    }

    private PropertyVO convert(Property property, boolean isForm) {
        return property.asViewObject(PropertyVO.class, v -> {
            v.setFullAddress(property.getBuildingNumber() + "栋" + property.getRoomNumber() + "室");
            v.setType(typeMapper.selectById(property.getTypeId()).getDescription());
            if (!isForm) {
                v.setStatusDesc(property.getStatus().equals("occupied") ? "已入住" : "空置中");
                if (property.getUserId() != null) v.setOwnerName(accountMapper.selectById(property.getUserId()).getRealName());
            }
        });
    }
}
