package com.example.entity.dto.in;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Date;

@Data
public class PropertyImportDTO {
    @ExcelProperty(index = 0, value = "楼栋号")
    @NotBlank(message = "楼栋号不能为空")
    private String buildingNumber;

    @ExcelProperty(index = 1, value = "房间号")
    @NotBlank(message = "房间号不能为空")
    private String roomNumber;

    @ExcelProperty(index = 2, value = "建筑面积(㎡)")
    @DecimalMin(value = "10.00", message = "面积最小10平方米")
    private Double floorArea;

    @ExcelProperty(index = 3, value = "房产所属人")
    private String ownerName;

    @ExcelProperty(index = 4, value = "购置时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat("yyyy-MM-dd")
    private Date perchaseDate;
}
