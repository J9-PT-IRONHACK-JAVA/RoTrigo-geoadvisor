package com.ironhack.geoadvisor;

import com.ironhack.geoadvisor.dto.PlaceDetails;
import com.ironhack.geoadvisor.dto.PlacesResult;
import com.ironhack.geoadvisor.proxy.GeocodingProxy;
import com.ironhack.geoadvisor.proxy.PlacesProxy;
import com.ironhack.geoadvisor.service.ConsoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import static java.lang.System.exit;

@Profile("!test")
@Component
@RequiredArgsConstructor
public class AppStarter implements CommandLineRunner {
    private final GeocodingProxy geocodingProxy;
    private final PlacesProxy placesProxy;
    private final ConsoleService consoleSVC;

    @Value("${gmaps.apiKey}")
    private String apiKey;

    @Override
    public void run(String... args) {
        var address = consoleSVC.ask("Please introduce address to locate:");
        var geocodeResponse = geocodingProxy.getLocation(apiKey, address);

        if (geocodeResponse != null && geocodeResponse.getStatus().equals("OK")) {
            var location = geocodeResponse.getResults().get(0).getGeometry().getLocation();
            System.out.printf("This is the location:\n %s\n", location);

            var placesResponse = placesProxy.getRestaurants(
                    apiKey,
                    "%s,%s".formatted(location.getLatitude(), location.getLongitude()),
                    "restaurant",
                    "500",
                    null
            );
            if (placesResponse != null && placesResponse.getStatus().equals("OK")) {
                var restaurants = placesResponse.getResults();
                System.out.println("These are the restaurants nearby:");

                for (PlacesResult r : restaurants) {
                    System.out.println(r);
                }

                var placeId = restaurants.get(0).getPlaceId();

                var placeResponse = placesProxy.getRestaurantDetails(
                        apiKey,
                        placeId,
                        "formatted_phone_number,website,serves_vegetarian_food"
                        );

                System.out.println(placeResponse.getResult());
            }
            System.out.println(placesResponse);


        } else {
            System.out.println("Location not found :(");
        }
        exit(0);
    }
}