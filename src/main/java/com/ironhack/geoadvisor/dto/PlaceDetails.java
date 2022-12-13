package com.ironhack.geoadvisor.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class PlaceDetails {
    @JsonProperty("formatted_phone_number")
    private String formattedPhoneNumber;
    private String website;
    @JsonProperty("serves_vegetarian_food")
    private Boolean servesVegetarianFood;
}
