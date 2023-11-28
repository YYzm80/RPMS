package com.example.entity.weather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Lives implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String city;
    private String weather;
    private String temperature;
    private String winddirection;
    private String windpower;

}
