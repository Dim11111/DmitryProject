package com.example.dmitryproject.repositories;

import com.example.dmitryproject.models.Tour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TourRepository extends JpaRepository<Tour, Integer> {
    List<Tour> findByTitleContainingIgnoreCase(String name);

    // Поиск по наименованию и фильтрация по указанному диапазону цены От - До
    // !ВАЖНО! - если в БД наименование колонки отличается от названия поля в модели товара, то в запросе пишем ТОЧНО ТАК КАК ОНА НАЗЫВАЕТСЯ В БД, т.е. если в модели мы в поле указали название @Column(name = "Наименование"), то в postgre столбец будет назван "наименование" и вот записывать надо именно в нижнем регистре, иначе статус 500.
    //Маски: %?1% означает, что искомое значение (?1 = String title) может быть в середине строки, ?1% - в начале строки, '%?1' - в конце строки
    // price >=?2 and price <=?3 : указано, что должен быть диапазон между вторым и третьем параметром метода (float ot, float Do).
    //NativeQuery = true включает методы с применением SQL
    //Обобщение: выбрать все из таблицы product, где слово/фраза (title) может быть в середине или конце или начале строки И (price) должна быть от принятого параметра float ot до float Do)
    @Query(value = "select * from tour where ((lower(title) LIKE %?1%) or (lower(title) LIKE '?1%') or (lower(title) LIKE '%?1')) and (price >= ?2 and price <=?3)", nativeQuery = true)
    List<Tour> findByTitleAndPriceGreaterThanEqualAndPriceLessThanEqual(String title, float on, float off);

    @Query(value = "select * from tour where ((lower(title) LIKE %?1%) or (lower(title) LIKE '?1%') OR (lower(title) LIKE '%?1')) and (price >= ?2 and price <= ?3) order by price",nativeQuery = true)
    List<Tour> findByTitleOrderByPriceAsc(String title, float on, float off);

    @Query(value = "select * from tour where ((lower(title) LIKE %?1%) or (lower(title) LIKE '?1%') OR (lower(title) LIKE '%?1')) and (price >= ?2 and price <= ?3) order by price desc",nativeQuery = true)
    List<Tour> findByTitleOrderByPriceDesc(String title, float on, float off);

    @Query(value = "select * from tour where location_id = ?4 and ((lower(title) LIKE %?1%) or (lower(title) LIKE '?1%') OR (lower(title) LIKE '%?1')) and (price >=?2 and price <=?3) order by price",nativeQuery = true)
    List<Tour> findByTitleAndLocationOrderByPriceAsc(String title, float on, float off, int location);

    @Query(value = "select * from tour where location_id = ?4 and ((lower(title) LIKE %?1%) or (lower(title) LIKE '?1%') OR (lower(title) LIKE '%?1')) and (price >= ?2 and price <= ?3) order by price desc",nativeQuery = true)
    List<Tour> findByTitleAndLocationOrderByPriceDesc(String title, float on, float off, int location);


}
