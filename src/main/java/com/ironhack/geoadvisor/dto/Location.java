package com.ironhack.geoadvisor.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

@Data
@ToString
public class Location {
    @JsonProperty("lat")
    private BigDecimal latitude;
    @JsonProperty("lng")
    private BigDecimal longitude;
}
