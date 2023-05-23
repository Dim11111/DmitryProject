package com.example.dmitryproject.services;

import com.example.dmitryproject.enumm.Status;
import com.example.dmitryproject.models.Location;
import com.example.dmitryproject.models.Order;
import com.example.dmitryproject.models.Tour;
import com.example.dmitryproject.repositories.OrderRepository;
import com.example.dmitryproject.repositories.TourRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class TourService {
    private final TourRepository tourRepository;

    private final OrderRepository orderRepository;

    public TourService(TourRepository tourRepository, OrderRepository orderRepository) {
        this.tourRepository = tourRepository;
        this.orderRepository = orderRepository;
    }

    public List<Tour> infoAllTours(){
        return tourRepository.findAll();
    }

    public Tour infoTour(int id){
        Optional<Tour> optionalTour = tourRepository.findById(id);
        return optionalTour.orElse(null);
    }
    @Transactional
    public void saveTour(Tour tour, Location location){
        tour.setLocation(location);
        tourRepository.save(tour);
    }

    @Transactional
    public void editTour(int id, Tour tour){
        tour.setId(id);
        tourRepository.save(tour);
    }

    @Transactional
    public void deleteTour(int id){
        tourRepository.deleteById(id);
    }

}
