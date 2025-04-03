package com.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ImportError {
    private Integer rowNumber;  // 错误行号
    private String message;     // 错误描述
}
