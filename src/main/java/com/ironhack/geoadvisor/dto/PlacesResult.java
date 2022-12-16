package com.ironhack.geoadvisor.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class PlacesResult {
    @JsonProperty("place_id")
    private String placeId;
    private String name;
    @JsonProperty("business_status")
    private String businessStatus;
    private Geometry geometry;
    private Double rating;
    @JsonProperty("price_level")
    private Integer priceLevel;
    @JsonProperty("user_ratings_total")
    private Integer userRatingsTotal;
    private String vicinity;
}
