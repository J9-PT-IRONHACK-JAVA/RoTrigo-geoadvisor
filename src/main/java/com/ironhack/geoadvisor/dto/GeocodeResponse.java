package com.ironhack.geoadvisor.dto;

import lombok.Data;

import java.util.List;

@Data
public class GeocodeResponse {
    private List<GeocodeResult> results;
    private String status;
}

