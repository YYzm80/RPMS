package com.example.controller;

import com.example.entity.RestBean;
import com.example.entity.dto.common.Types;
import com.example.service.TypeService;
import com.example.util.consts.Const;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/type")
public class TypeController {
    @Resource
    private TypeService service;

    @Operation(summary = "根据type获取所有分类")
    @RolesAllowed({Const.ROLE_ADMIN, Const.ROLE_MANAGER})
    @GetMapping("/all/{type}")
    public RestBean<List<Types>> all(@PathVariable("type") String type) {
        return RestBean.success(service.getTypeList(type));
    }

    @Operation(summary = "根据type获取所有启用的分类")
    @RolesAllowed({Const.ROLE_ADMIN, Const.ROLE_MANAGER})
    @GetMapping("/all-active/{type}")
    public RestBean<List<Types>> allActive(@PathVariable("type") String type) {
        return RestBean.success(service.getTypeListActive(type));
    }

    @Operation(summary = "根据id获取分类")
    @RolesAllowed({Const.ROLE_ADMIN, Const.ROLE_MANAGER})
    @GetMapping("/tid/{tid}")
    public RestBean<Types> tid(@PathVariable("tid") Long tid) {
        return RestBean.success(service.getTypeById(tid));
    }

    @Operation(summary = "添加分类")
    @RolesAllowed({Const.ROLE_ADMIN, Const.ROLE_MANAGER})
    @PostMapping("/add")
    public RestBean<String> add(@RequestBody Types types) {
        String s = service.addType(types);
        return s == null ? RestBean.success("分类添加成功") : RestBean.failure(400, s);
    }

    @Operation(summary = "更新分类")
    @RolesAllowed({Const.ROLE_ADMIN, Const.ROLE_MANAGER})
    @PutMapping("/update")
    public RestBean<String> update(@RequestBody Types types) {
        String s = service.updateType(types);
        return s == null ? RestBean.success("分类信息更新成功") : RestBean.failure(400, s);
    }

    @Operation(summary = "改变分类启用状态")
    @RolesAllowed({Const.ROLE_ADMIN, Const.ROLE_MANAGER})
    @PutMapping("/change-status")
    public RestBean<String> changeStatus(@RequestBody Long tid) {
        String s = service.changeTypeStatus(tid);
        return s == null ? RestBean.success("分类状态更新成功") : RestBean.failure(400, s);
    }

    @Operation(summary = "删除分类")
    @RolesAllowed({Const.ROLE_ADMIN})
    @PostMapping("/delete")
    public RestBean<String> delete(@RequestBody Long tid) {
        String s = service.deleteType(tid);
        return s == null ? RestBean.success("分类删除成功") : RestBean.failure(400, s);
    }
}
