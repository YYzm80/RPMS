package com.example.entity.dto.common;

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
@TableName("static")
public class Static implements Serializable, BaseData {

    /**
     * 统计id
     */
    @TableId(type = IdType.AUTO)
    private Integer sid;
    /**
     * 参与企业数量
     */
    private Integer mid;
    private Integer companyNum;
    /**
     * 发布岗位数量
     */
    private Integer recruitmentNum;
    /**
     * 参与学生数量
     */
    private Integer studentNum;
    /**
     * 共投出简历数量
     */
    private Integer resumeNum;
    private Integer interviewNum;
    /**
     * 投出简历/面试比例
     */
    private Double interviewRate;
    /**
     * 面试通过率
     */
    private Double interviewPassRate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    public Static() {

    }
}
