package com.example.dmitryproject.services;

import com.example.dmitryproject.models.Order;
import com.example.dmitryproject.repositories.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    //получаем лист заказов
    public List<Order> getAllOrders(){
        return orderRepository.findAll();
    }

    //получаем информацию о заказе
    public Order infoOrder(int id){
        Optional<Order> optionalOrder = orderRepository.findById(id);
        return optionalOrder.orElse(null);
    }

    //редактирование заказа
    @Transactional
    public void editOrder(int id, Order order){
        order.setId(id);
        orderRepository.save(order);
    }

}
