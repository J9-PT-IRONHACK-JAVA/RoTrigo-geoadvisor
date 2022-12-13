package com.ironhack.geoadvisor.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class PlacesResponse {
    private List<PlacesResult> results;
    private String status;
    @JsonProperty("next_page_token")
    private String nextPageToken;
}
