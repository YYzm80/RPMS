package com.example.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.example.entity.ImportResult;
import com.example.filter.ExcelDataFilter;
import com.example.service.DataService;
import com.example.util.ErrorRecorder;

import java.util.ArrayList;
import java.util.List;

public class FilterableExcelReader<T> implements ReadListener<T> {

    private final ExcelDataFilter<T> filter;
    private final List<T> validData = new ArrayList<>();

    private final DataService<T> dataService;

    public FilterableExcelReader(ExcelDataFilter<T> filter, DataService<T> dataService) {
        this.filter = filter;
        this.dataService = dataService;
    }

    @Override
    public void invoke(T data, AnalysisContext context) {
        int rowIndex = context.readRowHolder().getRowIndex() + 1; // Excel行号从1开始
        if (filter.filter(data, rowIndex)) {
            validData.add(data);
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        // 批量处理有效数据
        if (!validData.isEmpty()) {
            dataService.batchImport(validData);
        }
    }

    // 获取过滤结果
    public ImportResult getResult() {
        return new ImportResult(validData.size(), ErrorRecorder.getErrors());
    }
}
