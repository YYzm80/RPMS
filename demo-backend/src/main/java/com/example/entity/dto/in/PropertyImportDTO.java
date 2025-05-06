package com.example.entity.dto.in;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PropertyImportDTO {
    @ExcelProperty(index = 0, value = "楼栋号")
    @NotBlank(message = "楼栋号不能为空")
    @ColumnWidth(10)
    private String buildingNumber;

    @ExcelProperty(index = 1, value = "房间号")
    @NotBlank(message = "房间号不能为空")
    @ColumnWidth(10)
    private String roomNumber;

    @ExcelProperty(index = 2, value = "建筑面积(㎡)")
    @DecimalMin(value = "10.00", message = "面积最小10平方米")
    @ColumnWidth(13)
    private Double floorArea;

    @ExcelProperty(index = 3, value = "房产所属人")
    @ColumnWidth(15)
    private String ownerName;

    @ExcelProperty(index = 4, value = "购置时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat("yyyy-MM-dd")
    @ColumnWidth(15)
    private Date perchaseDate;

}
