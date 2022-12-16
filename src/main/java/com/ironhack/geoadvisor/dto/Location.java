package com.ironhack.geoadvisor.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

@Data
@ToString
@NoArgsConstructor
public class Location {
    @JsonProperty("lat")
    private BigDecimal latitude;
    @JsonProperty("lng")
    private BigDecimal longitude;

    private String address;

    public Location(BigDecimal latitude, BigDecimal longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
