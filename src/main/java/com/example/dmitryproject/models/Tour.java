package com.example.dmitryproject.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="Tour")
public class Tour {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;
    @Column(name="title")
    @NotEmpty(message = "Заполните наименование тура")
    private String title;
    @Column(name="price")
    @Min(value=1000, message = "Минимальная цена тура 1000руб")
    private float price;
    @NotEmpty(message = "Заполните описание тура")
    @Column(name="description")
    private String description;
    @NotEmpty(message = "Заполните имя туроператора")
    @Column(name="agency")
    private String agency;
    private LocalDateTime dateTime;
    @PrePersist
    private void init(){
        dateTime = LocalDateTime.now();
    }

    @ManyToMany()
    @JoinTable(name="tour_basket", joinColumns = @JoinColumn(name = "tour_id"), inverseJoinColumns = @JoinColumn(name="person_id"))
    private List<Person> personList;

    @ManyToOne(optional = false)
    private Location location;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "tour")
    private List<Image> imageList = new ArrayList<>();

    @OneToMany(mappedBy = "tour", fetch = FetchType.EAGER)
    private List<Order> orderList;

    // this указывает для какого продукта данная фотография
    public void addImageToTour(Image image){
        image.setTour(this);
        imageList.add(image);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAgency() {
        return agency;
    }

    public void setAgency(String agency) {
        this.agency = agency;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public List<Person> getPersonList() {
        return personList;
    }

    public void setPersonList(List<Person> personList) {
        this.personList = personList;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public List<Image> getImageList() {
        return imageList;
    }

    public void setImageList(List<Image> imageList) {
        this.imageList = imageList;
    }
}
