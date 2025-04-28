package com.example.controller;

import com.example.entity.RestBean;
import com.example.entity.dto.common.Repair;
import com.example.entity.vo.request.repair.RepairReq;
import com.example.entity.vo.response.RepairVO;
import com.example.service.RepairService;
import com.example.util.consts.Const;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/repair")
public class RepairController {

    @Resource
    private RepairService service;

    @Operation(summary = "获取所有报修信息")
    @GetMapping("/all")
    public RestBean<List<RepairVO>> all() {
        return RestBean.success(service.getRepairList());
    }

    @Operation(summary = "根据用户id获取报修信息")
    @GetMapping("/uid/{uid}")
    public RestBean<List<RepairVO>> getByUid(@PathVariable("uid") Long uid) {
        return RestBean.success(service.getRepairListByUserId(uid));
    }

    @Operation(summary = "根据id获取报修信息")
    @GetMapping("/rid/{rid}")
    public RestBean<RepairVO> get(@PathVariable("rid") Long rid) {
        return RestBean.success(service.getRepairById(rid));
    }

    @Operation(summary = "创建报修信息")
    @RolesAllowed({Const.ROLE_ADMIN, Const.ROLE_OWNER})
    @PostMapping("/add")
    public RestBean<String> add(@RequestBody @Valid Repair repair) {
        String s = service.addRepair(repair);
        return s == null ? RestBean.success("报修申请成功") : RestBean.failure(400, s);
    }

    @Operation(summary = "更新报修信息")
    @RolesAllowed({Const.ROLE_ADMIN, Const.ROLE_MANAGER, Const.ROLE_OWNER})
    @PutMapping("/update")
    public RestBean<String> update(@RequestBody Repair repair,
                                   @RequestParam(value = "price", required = false) BigDecimal price) {
        RepairReq req = new RepairReq();
        req.setRepair(repair);
        req.setPrice(price);
        String s = service.updateRepair(req);
        if (s == null) {
            if (price != null && price.compareTo(BigDecimal.ZERO) > 0) {
                return RestBean.success("报修状态更新成功，对应报修账单已推送");
            }
            return RestBean.success("报修状态更新成功");
        }
        return RestBean.failure(400, s);
    }

    @Operation(summary = "取消报修信息")
    @RolesAllowed({Const.ROLE_ADMIN, Const.ROLE_OWNER})
    @PostMapping("/delete")
    public RestBean<String> delete(@RequestBody Long rid) {
        String s = service.deleteRepair(rid);
        return s == null ? RestBean.success("取消报修申请成功") : RestBean.failure(400, s);
    }

}
