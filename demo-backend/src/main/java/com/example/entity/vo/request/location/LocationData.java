package com.example.entity.vo.request.location;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LocationData implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String status;
    private String info;
    private String province;
    private String city;
    private String adcode;
}
