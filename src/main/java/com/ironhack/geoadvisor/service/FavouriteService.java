package com.ironhack.geoadvisor.service;

import com.ironhack.geoadvisor.model.Restaurant;
import com.ironhack.geoadvisor.repository.FavouriteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FavouriteService {
    private final FavouriteRepository repository;
    public List<Restaurant> getAll() {
        return repository.findAll();
    }

    public boolean exists(Restaurant restaurant) {
        return repository.existsByPlaceId(restaurant.getPlaceId());
    }

    public void saveOrUpdate(Restaurant restaurant) {
        restaurant.setFavourite(true);
        repository.save(restaurant);
    }
}
