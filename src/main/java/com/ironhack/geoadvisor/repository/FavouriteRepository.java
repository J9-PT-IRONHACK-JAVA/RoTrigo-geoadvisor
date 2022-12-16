package com.ironhack.geoadvisor.repository;

import com.ironhack.geoadvisor.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavouriteRepository extends JpaRepository<Restaurant, String> {
}
