package com.ironhack.geoadvisor.service;

import com.ironhack.geoadvisor.dto.*;
import com.ironhack.geoadvisor.enums.GmapsApiStatus;
import com.ironhack.geoadvisor.model.Restaurant;
import com.ironhack.geoadvisor.proxy.GeocodingProxy;
import com.ironhack.geoadvisor.proxy.PlacesProxy;
import com.sun.jdi.request.InvalidRequestStateException;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

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

    public String getAddress(Location location) throws Exception {
        var formattedLocation = "%s,%s".formatted(location.getLatitude(),location.getLongitude());
        var response = geocodingProxy.getInverseLocation(apiKey, formattedLocation);
        processResponseStatus(response);
        if (response.getResults().size() == 0) return null;
        ArrayList<String> addressResults = (ArrayList<String>)response.getResults().stream().map(GeocodeResult::getFormattedAddress).toList();

        for (int i = addressResults.size()-1; i >= 0; i--) {
            var toRemove = addressResults.get(i);
            for (int j = i-1; j >= 0; j--) {
                var address = addressResults.get(j).replace(toRemove, "");
                addressResults.remove(j);
                addressResults.add(j, address);
            }
        }
        return String.join("\n", addressResults);
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
        var restaurants = new ArrayList<>(response.getResults().stream().map(this::mapRestaurant).toList());
        var nextPageToken = response.getNextPageToken();
        getNextRestaurants(nextPageToken, restaurants);
        sortByRating(restaurants);
        return restaurants;
    }

    // Calls Gmaps proxy to get next page of restaurants controlling exceptions
    private void getNextRestaurants(String nextPageToken, ArrayList<Restaurant> restaurants) throws Exception {
        while (nextPageToken != null) {
            TimeUnit.MILLISECONDS.sleep(1000);
            var response = placesProxy.getNextRestaurants(apiKey, nextPageToken);
            try {
                processResponseStatus(response);
            } catch (InvalidRequestStateException e) {
                continue;
            }
            restaurants.addAll(response.getResults().stream().map(this::mapRestaurant).toList());
            nextPageToken = response.getNextPageToken();
        }
    }


    private void sortByRating(ArrayList<Restaurant> restaurants) {
        restaurants.sort((r1, r2) -> {
            var rating1 = (r1.getRating() == null) ? 0 : r1.getRating();
            var rating2 = (r2.getRating() == null) ? 0 : r2.getRating();
            if (rating1 == rating2)
                return 0;
            return rating1 > rating2 ? -1 : 1;
        });
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
            case INVALID_REQUEST -> throw new InvalidRequestStateException("Too fast");
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
