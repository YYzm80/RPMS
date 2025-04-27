package com.example.entity.vo.request.repair;

import com.example.entity.dto.common.Repair;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class RepairReq {
    private Repair repair;
    private BigDecimal price;

    public RepairReq() {

    }
}
