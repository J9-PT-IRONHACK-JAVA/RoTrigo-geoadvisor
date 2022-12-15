package com.ironhack.geoadvisor.model;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String placeId;
    private String name;
    private Double rating;
    private Integer priceLevel;
    private String address;
    private String phoneNumber;
    private String website;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private boolean isFavourite = false;

    public Restaurant(String placeId, String name, Double rating, Integer priceLevel, String address) {
        this.placeId = placeId;
        this.name = name;
        this.rating = rating;
        this.priceLevel = (priceLevel == null) ? 0 : priceLevel;
        this.address = address;
    }

    public boolean hasDetails(){
        return this.phoneNumber == null;
    }
    // TODO toString() method for nice printing
}
