package com.ironhack.geoadvisor.service;

import com.ironhack.geoadvisor.dto.*;
import com.ironhack.geoadvisor.enums.GmapsApiStatus;
import com.ironhack.geoadvisor.model.Restaurant;
import com.ironhack.geoadvisor.proxy.GeocodingProxy;
import com.ironhack.geoadvisor.proxy.PlacesProxy;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GmapsService {
    @Value("${gmaps.apiKey}")
    private String apiKey;
    private final GeocodingProxy geocodingProxy;
    private final PlacesProxy placesProxy;
    private final FavouriteService favouriteSVC;

    public Location getLocation(String address) throws Exception {
        var formattedAddress = address.replace("\s", "+");
        var response = geocodingProxy.getLocation(apiKey, formattedAddress);
        processResponseStatus(response);
        if (response.getResults().size() == 0) return null;
        return response.getResults().get(0).getGeometry().getLocation();
    }

    public List<Restaurant> getNearbyRestaurants(Location location, int radius, String keyword)
            throws Exception {
        String formattedLocation = "%s,%s".formatted(location.getLatitude(), location.getLongitude());
        var response = placesProxy.getRestaurants(
                apiKey,
                formattedLocation,
                "restaurant",
                String.valueOf(radius),
                keyword
        );
        processResponseStatus(response);
        return response.getResults().stream().map(this::mapRestaurant).toList();
    }

    public Restaurant getRestaurantDetails(Restaurant restaurant) throws Exception {
        var response = placesProxy.getRestaurantDetails(
                apiKey,
                restaurant.getPlaceId(),
                "formatted_phone_number,website,serves_vegetarian_food"
        );
        processResponseStatus(response);
        var details = response.getResult();
        restaurant.setWebsite(details.getWebsite());
        restaurant.setPhoneNumber(details.getFormattedPhoneNumber());
        return restaurant;
    }

    private void processResponseStatus(Object response) throws Exception {
        if (response == null) throw new Exception("Error: Gmaps API not responding");
        String statusResponse = "";
        if (response instanceof GeocodeResponse) statusResponse = ((GeocodeResponse) response).getStatus();
        if (response instanceof PlacesResponse) statusResponse = ((PlacesResponse) response).getStatus();
        if (response instanceof PlaceDetailsResponse) statusResponse = ((PlaceDetailsResponse) response).getStatus();

        var status = GmapsApiStatus.valueOf(statusResponse);
        switch (status) {
            case ACCESS_DENIED -> throw new Exception("Error: access denied to Gmaps API");
            case OK, ZERO_RESULTS -> {}
            default -> throw new Exception("Error: Gmaps API error %s\n".formatted(status));
        }
    }

    private Restaurant mapRestaurant(PlacesResult result) {
        var restaurant = new Restaurant(
                result.getPlaceId(),
                result.getName(),
                result.getRating(),
                result.getPriceLevel(),
                result.getVicinity()
        );
        restaurant.setFavourite(favouriteSVC.exists(restaurant));
        return restaurant;
    }
}
