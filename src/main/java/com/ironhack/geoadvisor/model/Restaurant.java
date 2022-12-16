package com.ironhack.geoadvisor.model;

import com.google.gson.annotations.Expose;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@Entity
@NoArgsConstructor
@Table(name = Restaurant.TABLE_NAME)
public class Restaurant {
    public static final String TABLE_NAME = "favourite_restaurants";

    @Id
    private String Id;

    @Expose
    private String name;
    @Expose
    private Double rating;
    @Expose
    private Integer reviewsNumber;
    @Expose
    private Integer priceLevel;
    @Expose
    private String address;
    @Expose
    private String phoneNumber;
    private String website;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private boolean isFavourite = false;

    public Restaurant(String Id, String name, Double rating, Integer reviewsNumber, Integer priceLevel,
                      String address, BigDecimal latitude, BigDecimal longitude) {
        this.Id = Id;
        this.name = name;
        this.rating = rating;
        this.reviewsNumber = reviewsNumber;
        this.priceLevel = (priceLevel == null) ? 0 : priceLevel;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public boolean hasDetails(){
        return this.phoneNumber != null;
    }

    public String toString(){
        var text ="""
                Name         = %s
                Rating       = %s
                Reviews      = %s
                Price level  = %s
                Address      = %s
                Favourite    = %s
                """.formatted(
                        getName(),
                        getRating(),
                        getReviewsNumber(),
                        getPriceLevel(),
                        getAddress(),
                        isFavourite()?"YES":"NO"
        );

        if (hasDetails()) {
            text += """
                    Phone number = %s
                    Website      = %s
                    """.formatted(getPhoneNumber(), getWebsite());
        }
        return text;
    }
}
