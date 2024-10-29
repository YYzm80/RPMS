package com.example.entity.dto.interview;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.entity.BaseData;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
@TableName("interview")
public class Interview implements Serializable, BaseData {

    /**
     * 面试信息id
     */
    @TableId(type = IdType.AUTO)
    private Integer iid;
    /**
     * 面试学生id
     */
    private Integer uid;
    /**
     * 面试岗位id
     */
    private Integer rid;
    /**
     * 面试状态
     */
    private String state;


}
