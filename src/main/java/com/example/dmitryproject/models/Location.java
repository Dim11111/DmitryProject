package com.example.dmitryproject.models;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    @OneToMany(mappedBy = "location", fetch = FetchType.EAGER)
    private List<Tour> tour;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Tour> getTour() {
        return tour;
    }

    public void setTour(List<Tour> tour) {
        this.tour = tour;
    }
}
