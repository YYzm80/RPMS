package com.example.controller;

import com.example.entity.RestBean;
import com.example.entity.dto.common.Repair;
import com.example.entity.vo.response.RepairVO;
import com.example.service.RepairService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/repair")
public class RepairController {

    @Resource
    private RepairService service;

    @GetMapping("/all")
    public RestBean<List<RepairVO>> all() {
        return RestBean.success(service.getRepairList());
    }

    @GetMapping("/uid/{uid}")
    public RestBean<List<RepairVO>> getByUid(@PathVariable("uid") Long uid) {
        return RestBean.success(service.getRepairListByUserId(uid));
    }

    @GetMapping("/rid/{rid}")
    public RestBean<RepairVO> get(@PathVariable("rid") Long rid) {
        return RestBean.success(service.getRepairById(rid));
    }

    @PostMapping("/add")
    public RestBean<String> add(@RequestBody Repair repair) {
        String s = service.addRepair(repair);
        return s == null ? RestBean.success("报修申请成功") : RestBean.failure(400, s);
    }

    @PostMapping("/update")
    public RestBean<String> update(@RequestBody Repair repair) {
        String s = service.updateRepair(repair);
        return s == null ? RestBean.success("报修状态更新成功") : RestBean.failure(400, s);
    }

    @PostMapping("/delete")
    public RestBean<String> delete(@RequestBody Long rid) {
        String s = service.deleteRepair(rid);
        return s == null ? RestBean.success("取消报修申请成功") : RestBean.failure(400, s);
    }

}
