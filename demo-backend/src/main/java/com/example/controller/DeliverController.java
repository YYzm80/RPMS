package com.example.controller;

import com.example.entity.RestBean;
import com.example.entity.dto.interview.Delivery;
import com.example.entity.vo.response.DeliveryVO;
import com.example.service.DeliveryService;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/deliver")
public class DeliverController {
    @Resource
    private DeliveryService service;

    @GetMapping("/all-uid/{uid}")
    public RestBean<List<DeliveryVO>> allUid(@PathVariable("uid") Integer uid) {
        return RestBean.success(service.getAllByUid(uid));
    }

    @GetMapping("/all-cid/{cid}")
    public RestBean<List<DeliveryVO>> allCid(@PathVariable("cid") Integer cid) {
        return RestBean.success(service.getAllByCid(cid));
    }

    @GetMapping("/{did}")
    public RestBean<DeliveryVO> did(@PathVariable("did") Integer did) {
        return RestBean.success(service.getByDid(did));
    }

    @PostMapping("/add")
    @PreAuthorize("hasAnyRole('admin', 'student')")
    public RestBean<String> add(Delivery delivery) {
        String s = service.addDelivery(delivery);
        return s == null ? RestBean.success("投递成功，请等待结果") : RestBean.failure(400, s);
    }

    @PostMapping("/update")
    @PreAuthorize("hasAnyRole('admin', 'student', 'company')")
    public RestBean<String> update(Delivery delivery) {
        String s = service.updateDelivery(delivery);
        return s == null ? RestBean.success("投递简历更换成功") : RestBean.failure(400, s);
    }

    @PostMapping("/delete")
    @PreAuthorize("hasAnyRole('admin', 'student')")
    public RestBean<String> delete(Integer did) {
        String s = service.deleteByDid(did);
        return s == null ? RestBean.success("取消投递成功") : RestBean.failure(400, s);
    }
}
