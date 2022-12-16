package com.ironhack.geoadvisor.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CoordinatesService {

    /**
     * Sources
     * https://www.geeksforgeeks.org/program-distance-two-points-earth/
     * http://www.movable-type.co.uk/scripts/latlong.html
     *
     * @param lat1
     * @param lon2
     * @param lat2
     * @param lon2
     * @return
     */
    public double coordinatesDistance(double lat1, double lat2, double lon1, double lon2){
        // convert from degrees to radians
            lon1 = Math.toRadians(lon1);
            lon2 = Math.toRadians(lon2);
            lat1 = Math.toRadians(lat1);
            lat2 = Math.toRadians(lat2);

            // Haversine formula Distance, d = 6378.8 * arccos[(sin(lat1) * sin(lat2)) + cos(lat1) * cos(lat2) * cos(long2 – long1)]
            double dlon = lon2 - lon1;
            double dlat = lat2 - lat1;
            //a = sin²(Δφ/2) + cos φ1 ⋅ cos φ2 ⋅ sin²(Δλ/2)
            double a = Math.pow(Math.sin(dlat / 2), 2) + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(dlon / 2),2);
            //c = 2 ⋅ atan2( √a, √(1−a) )
            double c = 2 * Math.atan2(Math.sqrt(a),Math.sqrt(1-a));
            // Radius of earth in kilometers. Use 3956
            double r = 6371;

            return(c * r);
    }

    /**
     * https://stackoverflow.com/questions/13973485/how-to-find-mid-point-coordinates-of-some-number-of-gps-points
      * @param lat1
     * @param lon1
     * @param lat2
     * @param lon2
     * @return
     */
    /*public double[] midPoint(double lat1,double lon1,double lat2,double lon2) {
        double dLon = Math.toRadians(lon2-lon1);
        double Bx = Math.cos(lat2) * Math.cos(dLon);
        double By = Math.cos(lat2) * Math.sin(dLon);
        double lat3rad = Math.atan2(Math.sin(lat1)+Math.sin(lat2),Math.sqrt(Math.pow(Math.cos(lat1)+Bx,2) + Math.pow(By,2)) );
        double lon3rad = lon1 + Math.atan2(By, Math.cos(lat1) + Bx);
        double lat3=Math.toDegrees(lat3rad);
        double lon3=Math.toDegrees(lon3rad);
        return new double[]{lat3,lon3};
    }*/

    public double[] midPoint(String lat1,String lon1,String lat2,String lon2, String lat3, String lon3) {
        var lat1i= new BigDecimal(lat1);
        var lat2i= new BigDecimal(lat2);
        var lat3i= new BigDecimal(lat3);
        var lon1i= new BigDecimal(lon1);
        var lon2i= new BigDecimal(lon2);
        var lon3i= new BigDecimal(lon3);
        var latSum = lat1i.add(lat2i);
        latSum =latSum.add(lat3i);
        var lonSum = lon1i.add(lon2i).add(lon3i);

        var meanLat = latSum.divide(new BigDecimal("3"));
        var meanLon =  lonSum.divide(new BigDecimal("3"));
        return new double[] {meanLat.doubleValue(),meanLon.doubleValue()};
    }

    /*public double[] midPoint(double lat1,double lon1,double lat2,double lon2) {
        double meanLat = (lat1 + lat2) / 2;
        double f1=Math.tan((Math.PI/4)+(lat1/2));
        double f2 =Math.tan((Math.PI/4)+(lat2/2));
        double lat3 =Math.tan((Math.PI/4)+(meanLat/2));
        double lon3 =( (lon2-lon1)*Math.log(lat3) + lon1*Math.log(f2) - lon2*Math.log(f1) ) / Math.log(f2/f1);


        return new double[]{lat3, lon3};
    }*/



}
