package com.example.dmitryproject.repositories;

import com.example.dmitryproject.models.Basket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository
public interface BasketRepository extends JpaRepository <Basket, Integer>{
    List<Basket> findByPersonId(int id);

    @Modifying
    @Query(value = "delete from tour_basket where tour_id=?1", nativeQuery = true)
    void deleteBasketByTourId(int id);
}
