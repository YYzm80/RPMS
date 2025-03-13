package com.example.entity;

import lombok.Data;

import java.util.List;


@Data
public class ImportResult {
    private final int successCount;
    private final List<String> errors;

}
