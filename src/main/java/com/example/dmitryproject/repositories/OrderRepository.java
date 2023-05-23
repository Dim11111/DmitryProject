package com.example.dmitryproject.repositories;

import com.example.dmitryproject.models.Order;
import com.example.dmitryproject.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findByPerson(Person person);

    //NativeQuery = true включает методы с применением SQL
    //'%?1' - означает, что number ищем в конце строки
    //?1 - указывает, что это первый параметр нашего метода (String number);
    // lower = приводим в нижний регистр
    @Query(value = "select * from orders where number LIKE %?1", nativeQuery = true)
    List<Order> findOrderByNumberIgnoreCase(String number);

}
