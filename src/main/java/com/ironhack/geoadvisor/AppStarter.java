package com.ironhack.geoadvisor;

import com.ironhack.geoadvisor.dto.PlaceDetails;
import com.ironhack.geoadvisor.dto.PlacesResult;
import com.ironhack.geoadvisor.proxy.GeocodingProxy;
import com.ironhack.geoadvisor.proxy.PlacesProxy;
import com.ironhack.geoadvisor.service.ConsoleService;
import com.ironhack.geoadvisor.service.FlowService;
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
    private final FlowService flowSVC;

    @Override
    public void run(String... args) {
        flowSVC.start();
    }
}