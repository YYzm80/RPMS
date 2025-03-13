package com.example.controller;

import com.alibaba.excel.EasyExcel;
import com.example.entity.ImportResult;
import com.example.entity.RestBean;
import com.example.entity.dto.common.Property;
import com.example.entity.dto.in.PropertyImportDTO;
import com.example.entity.vo.response.PropertyVO;
import com.example.filter.PropertyImportFilter;
import com.example.listener.FilterableExcelReader;
import com.example.service.DataService;
import com.example.service.PropertyService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/property")
public class PropertyController {

    @Resource
    private PropertyService service;

    @Resource
    private PropertyImportFilter filter;

    @Resource
    private DataService<PropertyImportDTO> propertyImportService;

    @GetMapping("/all")
    public RestBean<List<PropertyVO>> all() {
        return RestBean.success(service.getPropertyList());
    }

    @GetMapping("/pid/{pid}")
    public RestBean<PropertyVO> get(@PathVariable("pid") Long pid) {
        return RestBean.success(service.getPropertyVO(pid));
    }

    @PostMapping("/add")
    public RestBean<String> add(@RequestParam("file") MultipartFile file, Property property) {
        String s = service.addProperty(file, property);
        return s == null ? RestBean.success("新增房产信息成功") : RestBean.failure(400, s);
    }

    @PostMapping("/update")
    public RestBean<String> update(@RequestParam(value = "file", required = false) MultipartFile file,
                                   Property property) {
        String s = service.updateProperty(file, property);
        return s == null ? RestBean.success("更新房产信息成功") : RestBean.failure(400, s);
    }

    @PostMapping("/delete")
    public RestBean<String> delete(@RequestBody Long pid) {
        String s = service.deleteProperty(pid);
        return s == null ? RestBean.success("删除房产信息成功") : RestBean.failure(400, s);
    }

    @PostMapping("/import")
    public RestBean<String> importProperties(@RequestParam("file") MultipartFile file) {
        try {
            FilterableExcelReader<PropertyImportDTO> reader = new FilterableExcelReader<>(filter, propertyImportService);
            EasyExcel.read(file.getInputStream(), PropertyImportDTO.class, reader)
                    .sheet()
                    .doRead();
            return checkResult(reader);
        } catch (IOException e) {
            return RestBean.failure(400, "文件读取失败");
        }
    }

    private RestBean<String> checkResult(FilterableExcelReader<PropertyImportDTO> reader) {
        ImportResult result = reader.getResult();
        if (result.getErrors().isEmpty()) {
            return RestBean.success("成功导入" + result.getSuccessCount() + "条房产数据");
        } else {
            return RestBean.failure(400,
                    "成功导入" + result.getSuccessCount() + "条，失败详情：" +
                            String.join("；", result.getErrors()));
        }
    }

}
