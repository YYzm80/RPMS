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
import com.example.util.ExcelUtil;
import com.example.util.consts.Const;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import jakarta.annotation.security.RolesAllowed;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/property")
public class PropertyController {

    @Resource
    private PropertyService service;

    @Resource
    private PropertyImportFilter filter;

    @Resource
    private DataService<PropertyImportDTO> propertyImportService;

    @Operation(summary = "获取所有房产信息")
    @GetMapping("/all")
    public RestBean<List<PropertyVO>> all() {
        return RestBean.success(service.getPropertyList());
    }

    @Operation(summary = "根据id获取房产信息")
    @GetMapping("/pid/{pid}")
    public RestBean<PropertyVO> get(@PathVariable("pid") Long pid) {
        return RestBean.success(service.getPropertyVO(pid));
    }

    @Operation(summary = "下载房产信息excel表模版")
    @GetMapping("/template")
    public void template(HttpServletResponse response) {
        String fileName = "导入房产信息模板";
        String sheetName = "导入房产信息模板";
        List<PropertyImportDTO> propertyList = new ArrayList<>();
        propertyList.add(new PropertyImportDTO("2", "301", 58.3, "张三", "住宅", new Date()));
        propertyList.add(new PropertyImportDTO( "2", "302", 56.8, "", "商业房", new Date()));
        try {
            ExcelUtil.writeExcel(response, propertyList, fileName, sheetName, PropertyImportDTO.class);
        } catch (Exception e) {
            log.error("文件模版生成失败", e);
        }
    }

    @Operation(summary = "创建房产信息")
    @RolesAllowed({Const.ROLE_ADMIN, Const.ROLE_MANAGER})
    @PostMapping("/add")
    public RestBean<String> add(@RequestParam("file") MultipartFile file, @Valid Property property) {
        String s = service.addProperty(file, property);
        return s == null ? RestBean.success("新增房产信息成功") : RestBean.failure(400, s);
    }

    @Operation(summary = "更新房产信息")
    @RolesAllowed({Const.ROLE_ADMIN, Const.ROLE_MANAGER})
    @PostMapping("/update")
    public RestBean<String> update(@RequestParam(value = "file", required = false) MultipartFile file,
                                   @Valid Property property) {
        String s = service.updateProperty(file, property);
        return s == null ? RestBean.success("更新房产信息成功") : RestBean.failure(400, s);
    }

    @Operation(summary = "删除房产信息")
    @RolesAllowed({Const.ROLE_ADMIN, Const.ROLE_MANAGER})
    @PostMapping("/delete")
    public RestBean<String> delete(@RequestBody Long pid) {
        String s = service.deleteProperty(pid);
        return s == null ? RestBean.success("删除房产信息成功") : RestBean.failure(400, s);
    }

    @Operation(summary = "导入房产信息")
    @RolesAllowed({Const.ROLE_ADMIN, Const.ROLE_MANAGER})
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
