package com.ironhack.geoadvisor.proxy;
import com.ironhack.geoadvisor.dto.PlacesResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@FeignClient(name = "restaurants", url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json")
public interface PlacesProxy {
    @GetMapping()
    PlacesResponse getRestaurants(
            @RequestParam("key") String key,
            @RequestParam("location") String location,
            @RequestParam("type") String type,
            @RequestParam("radius") String radius,
            @RequestParam(value = "keyword", required = false) Optional<String> keyword
            );
}
