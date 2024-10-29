package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.dto.interview.Delivery;
import com.example.entity.vo.response.DeliveryVO;

import java.util.List;

public interface DeliveryService extends IService<Delivery> {

    List<DeliveryVO> getAllByUid(Integer uid);
    List<DeliveryVO> getAllByCid(Integer cid);
    DeliveryVO getByDid(Integer did);
    String addDelivery(Delivery delivery);
    String updateDelivery(Delivery delivery);
    String deleteByDid(Integer did);
}
