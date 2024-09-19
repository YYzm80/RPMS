package com.example.entity.weather;

import com.example.entity.vo.request.LivesVO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherResponse implements Serializable {
    private static final long serializationUID = 1L;
    private List<LivesVO> lives;      //消息数据
    private String status;          //消息状态
    private String info;            //消息描述
}
