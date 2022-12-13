package com.ironhack.geoadvisor;

import com.ironhack.geoadvisor.proxy.GeocodingProxy;
import com.ironhack.geoadvisor.service.ConsoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("!test")
@Component
public class AppStarter implements CommandLineRunner {
    @Autowired
    GeocodingProxy geocodingProxy;
    @Autowired
    ConsoleService consoleSVC;

    @Override
    public void run(String... args) {

        var address = consoleSVC.ask("Please introduce address to locate:");
        var geocodeResponse = geocodingProxy.getLocation(address, "AIzaSyAaD1TskQMYU6WhRsKDsvg0Qh4H5NY8XNg");

        if (geocodeResponse != null && geocodeResponse.getStatus().equals("OK")) {
            var location = geocodeResponse.getResults().get(0).getGeometry().getLocation();
            System.out.printf("This is the location:\n %s\n", location);
        } else {
            System.out.println("Location not found :(");
        }

    }
}