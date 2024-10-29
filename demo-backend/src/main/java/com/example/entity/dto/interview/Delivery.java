package com.example.entity.dto.interview;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.entity.BaseData;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@TableName("delivery")
public class Delivery implements Serializable, BaseData {

    /**
     * 投递id
     */
    @TableId(type = IdType.AUTO)
    private Integer did;
    /**
     * 投递人id
     */
    private Integer uid;
    /**
     * 投递岗位id
     */
    private Integer rid;
    /**
     * 投递简历id
     */
    private Integer resumeId;

    private String state;
    /**
     * 投递时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date deliveryTime;


}
