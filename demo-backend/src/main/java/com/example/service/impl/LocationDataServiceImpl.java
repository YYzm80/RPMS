package com.example.service.impl;

import com.example.entity.vo.request.location.LocationData;
import com.example.service.LocationDataService;
import com.example.util.HttpContextUtils;
import com.example.util.consts.Const;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Slf4j
@Service
public class LocationDataServiceImpl implements LocationDataService {
    @Resource
    RestTemplate restTemplate;

    @Override
    public LocationData getDataByIP() {
        return this.doGetLocationData(Const.LOCATION_API + "&ip=" + HttpContextUtils.getIpAddress());
    }

    private LocationData doGetLocationData(String url) {
        log.info("获取ip接口完整url：" + url);
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        String strBody = null;
        if (response.getStatusCode().value() == 200) {
            strBody = response.getBody();
        }
        ObjectMapper mapper = new ObjectMapper();
        LocationData location;
        try {
            location = mapper.readValue(strBody, LocationData.class);
        } catch (IOException e) {
            log.error("解析地理位置信息失败", e);
            return null;
        }
        return location;
    }
}
