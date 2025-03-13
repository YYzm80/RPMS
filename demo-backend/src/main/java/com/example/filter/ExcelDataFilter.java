package com.example.filter;

import org.springframework.stereotype.Component;

// Excel数据过滤器接口
@Component
public interface ExcelDataFilter<T> {
    /**
     * 过滤逻辑：返回true表示保留数据，false表示过滤
     */
    boolean filter(T data, Integer rowIndex);
}
