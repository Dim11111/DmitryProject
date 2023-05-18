package com.example.dmitryproject.models;

import jakarta.persistence.*;

@Entity
@Table(name="tour_basket")
public class Basket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "tour_id")
    private int tourId;
    @Column(name = "person_id")
    private int personId;

    public Basket(int tourId, int personId) {
        this.tourId = tourId;
        this.personId = personId;
    }

    public Basket() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTourId() {
        return tourId;
    }

    public void setTourId(int tourId) {
        this.tourId = tourId;
    }

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }
}
