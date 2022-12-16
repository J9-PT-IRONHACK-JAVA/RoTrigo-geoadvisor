package com.ironhack.geoadvisor.utils;

import com.ironhack.geoadvisor.dto.Location;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class Coordinates {

    /*public BigDecimal coordinatesDistance(Location location1, Location location2){
        // convert from degrees to radians
        var difLat = location1.getLatitude().subtract(location2.getLatitude());
        var difLon = location1.getLongitude().subtract(location2.getLongitude());

        var powLat = difLat.pow(2);
        var powLon = difLon.pow(2);
        var result = powLat.add(powLon);

        return result.sqrt(new MathContext(10));
    }*/




    public Location midPoint(Location... locations) {
        var midLatitude = new BigDecimal(0);
        var midLongitude = new BigDecimal(0);
        for (Location p : locations) {
            midLatitude = midLatitude.add(p.getLatitude());
            midLongitude = midLongitude.add(p.getLongitude());
        }
        midLatitude = midLatitude.divide(BigDecimal.valueOf(locations.length),8, RoundingMode.HALF_DOWN);
        midLongitude = midLongitude.divide(BigDecimal.valueOf(locations.length),8,RoundingMode.HALF_DOWN);

        var location = new Location();
        location.setLatitude(midLatitude);
        location.setLongitude(midLongitude);
        return location;
    }


    public double coordinatesDistance(Location location1, Location location2){
        var lat1 =location1.getLatitude().doubleValue();
        var lng1 = location1.getLongitude().doubleValue();
        var lat2 =location2.getLatitude().doubleValue();
        var lng2 = location2.getLongitude().doubleValue();

        double earthRadius = 6371.0;
        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);
        double sindLat = Math.sin(dLat / 2);
        double sindLng = Math.sin(dLng / 2);
        double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)* Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double dist = earthRadius * c;

        return dist;
    }




}
