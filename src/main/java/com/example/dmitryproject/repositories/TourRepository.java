package com.example.dmitryproject.repositories;

import com.example.dmitryproject.models.Tour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TourRepository extends JpaRepository<Tour, Integer> {
    List<Tour> findByTitleContainingIgnoreCase(String name);

    @Query(value = "select * from tour where (lower(title) LIKE %?1%) or (lower(title) LIKE '?1%') or (lower(title) LIKE '%?1') and (price >=?2 and price <=?3)", nativeQuery = true)
    List<Tour> findByTitleAndPriceGreaterThanEqualAndPriceLessThanEqual(String title, float on, float off);

    @Query(value = "select * from tour where (lower(title) LIKE %?1%) or (lower(title) LIKE '?1%') OR (lower(title) LIKE '%?1') and (price >= ?2 and price <= ?3) order by price",nativeQuery = true)
    List<Tour> findByTitleOrderByPriceAsc(String title, float on, float off);

    @Query(value = "select * from tour where (lower(title) LIKE %?1%) or (lower(title) LIKE '?1%') OR (lower(title) LIKE '%?1') and (price >= ?2 and price <= ?3) order by price desc",nativeQuery = true)
    List<Tour> findByTitleOrderByPriceDesc(String title, float on, float off);

    @Query(value = "select * from tour where location_id = ?4 and (lower(title) LIKE '%?1%') or (lower(title) LIKE '?1%') OR (lower(title) LIKE '%?1') and (price >= ?2 and price <= ?3) order by price",nativeQuery = true)
    List<Tour> findByTitleAndLocationOrderByPriceAsc(String title, float on, float off, int location);

    @Query(value = "select * from tour where location_id = ?4 and (lower(title) LIKE '%?1%') or (lower(title) LIKE '?1%') OR (lower(title) LIKE '%?1') and (price >= ?2 and price <= ?3) order by price desc",nativeQuery = true)
    List<Tour> findByTitleAndLocationOrderByPriceDesc(String title, float on, float off, int location);


}
