package com.example.dmitryproject.models;

import jakarta.persistence.*;

@Entity
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String fileName;
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private Tour tour;

    public Image(int id, String fileName, Tour tour) {
        this.id = id;
        this.fileName = fileName;
        this.tour = tour;
    }

    public Image() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Tour getTour() {
        return tour;
    }

    public void setTour(Tour tour) {
        this.tour = tour;
    }
}
