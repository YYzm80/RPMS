package com.example.service.impl;

import com.example.entity.vo.request.location.LocationData;
import com.example.service.LocationDataService;
import com.example.util.Const;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Service
public class LocationDataServiceImpl implements LocationDataService {
    @Resource
    RestTemplate restTemplate;

    @Override
    public LocationData getDataByIP() {
        return this.doGetLocationData(Const.LOCATION_API);
    }

    private LocationData doGetLocationData(String url) {
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
            e.printStackTrace();
            return null;
        }
        return location;
    }
}
