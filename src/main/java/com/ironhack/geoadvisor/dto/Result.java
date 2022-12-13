package com.ironhack.geoadvisor.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class Result {
    @JsonProperty("formatted_address")
    private String formattedAddress;
    private Geometry geometry;
}
