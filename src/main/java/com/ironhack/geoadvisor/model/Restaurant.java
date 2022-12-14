package com.ironhack.geoadvisor.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;

@Data
@Entity
@NoArgsConstructor
public class Restaurant {
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

    public Restaurant(String placeId, String name, Double rating, Integer priceLevel, String address) {
        this.placeId = placeId;
        this.name = name;
        this.rating = rating;
        this.priceLevel = priceLevel;
        this.address = address;
    }
}
