package com.example.entity.vo.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LivesVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String city;
    private String weather;
    private String temperature;
    private String winddirection;
    private String windpower;

}
