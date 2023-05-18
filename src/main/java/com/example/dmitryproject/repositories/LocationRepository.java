package com.example.dmitryproject.repositories;

import com.example.dmitryproject.models.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, Integer> {
    com.example.dmitryproject.models.Location findByName(String name);
}
