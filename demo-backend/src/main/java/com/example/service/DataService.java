package com.example.service;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public interface DataService<T> {
    void batchImport(List<T> list);
}