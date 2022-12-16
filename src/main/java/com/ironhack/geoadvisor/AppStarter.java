package com.ironhack.geoadvisor;

import com.ironhack.geoadvisor.dto.Location;
import com.ironhack.geoadvisor.dto.PlaceDetails;
import com.ironhack.geoadvisor.dto.PlacesResult;
import com.ironhack.geoadvisor.proxy.GeocodingProxy;
import com.ironhack.geoadvisor.proxy.PlacesProxy;
import com.ironhack.geoadvisor.service.ConsoleService;
import com.ironhack.geoadvisor.service.CoordinatesService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

import static java.lang.System.exit;

@Profile("!test")
@Component
@RequiredArgsConstructor
public class AppStarter implements CommandLineRunner {
    private final GeocodingProxy geocodingProxy;
    private final PlacesProxy placesProxy;
    private final ConsoleService consoleSVC;
    private final CoordinatesService coordSVC;

    @Value("${gmaps.apiKey}")
    private String apiKey;

    @Override
    public void run(String... args) {
        /*
        41.39379, 2.17102
        41.85708, 2.30125
        41.14071, 1.40217
        */

        var location1 = new Location(new BigDecimal("41.39379"), new BigDecimal("2.17102"));
        var location2 = new Location(new BigDecimal("41.85708"), new BigDecimal("2.30125"));
        var location3 = new Location(new BigDecimal("41.14071"), new BigDecimal("1.40217"));


        var distance = coordSVC.coordinatesDistance(location1, location2);
        System.out.println(distance.toString());

        var midPoint=coordSVC.midPoint(location1, location2, location3);
        //System.out.println(distance);
        System.out.println(midPoint.getLatitude());
        System.out.println(midPoint.getLongitude());



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