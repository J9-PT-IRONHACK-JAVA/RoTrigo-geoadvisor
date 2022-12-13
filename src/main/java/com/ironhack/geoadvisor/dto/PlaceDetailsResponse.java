package com.ironhack.geoadvisor.dto;

import lombok.Data;

@Data
public class PlaceDetailsResponse {
    private PlaceDetails result;
    private String status;
}
