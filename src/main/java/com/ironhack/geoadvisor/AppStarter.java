package com.ironhack.geoadvisor;

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
        var lat1 = consoleSVC.ask("Please introduce latitude 1:");
        var lon1 = consoleSVC.ask("Please introduce longitude 1:");
        var lat2 = consoleSVC.ask("Please introduce latitude 2:");
        var lon2 = consoleSVC.ask("Please introduce longitude 2:");
        var lat3 = consoleSVC.ask("Please introduce latitude 3:");
        var lon3 = consoleSVC.ask("Please introduce longitude 3:");

        //var distance =coordSVC.coordinatesDistance(Double.parseDouble(lat1),Double.parseDouble(lon1),Double.parseDouble(lat2),Double.parseDouble(lon2));
        //var midPoint=coordSVC.midPoint(Double.parseDouble(lat1),Double.parseDouble(lon1),Double.parseDouble(lat2),Double.parseDouble(lon2),Double.parseDouble(lat3),Double.parseDouble(lon3));
        var midPoint=coordSVC.midPoint(lat1,lon1,lat2,lon2,lat3,lon3);
        //System.out.println(distance);
        System.out.println(midPoint[0]);
        System.out.println(midPoint[1]);



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