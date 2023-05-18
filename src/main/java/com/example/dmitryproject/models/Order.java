package com.example.dmitryproject.models;

import com.example.dmitryproject.enumm.Status;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String number;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private Tour tour;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private Person person;

    private int count;

    private int price;

    private Status status;

    private LocalDateTime dateTime;

    @PrePersist
    private void init(){
        dateTime = LocalDateTime.now();
    }

    public Order(String number, Tour tour, Person person, int count, int price, Status status) {
        this.number = number;
        this.tour = tour;
        this.person = person;
        this.count = count;
        this.price = price;
        this.status = status;
    }

    public Order() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Tour getTour() {
        return tour;
    }

    public void setTour(Tour tour) {
        this.tour = tour;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
}
