package com.example.entity.dto.meeting;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.entity.BaseData;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@TableName("meeting")
public class Meeting implements Serializable, BaseData {

    /**
     * 双选会id
     */
    @TableId(type = IdType.AUTO)
    private Integer mid;
    /**
     * 双选会名称
     */
    @Length(max = 255, message = "编码长度不能超过255")
    private String name;
    private String img;
    /**
     * 双选会进行状态
     */
    private String state;
    /**
     * 开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;
    /**
     * 结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;

}
