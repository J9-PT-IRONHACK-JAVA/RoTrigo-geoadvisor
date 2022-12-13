package com.ironhack.geoadvisor.proxy;
import com.ironhack.geoadvisor.dto.GeocodeResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "geocoding", url = "https://maps.googleapis.com/maps/api/geocode/json")
public interface GeocodingProxy {
    @GetMapping()
    GeocodeResponse getLocation(@RequestParam("address") String address, @RequestParam("key") String key);
}
