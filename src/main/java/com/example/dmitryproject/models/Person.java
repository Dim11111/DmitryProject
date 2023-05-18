package com.example.dmitryproject.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name="Person")
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;
    @NotEmpty(message = "Имя пользователя не может быть пустым")
    @Size(min = 5, max = 20, message = "Имя пользователя может содержать от 5 до 20 символов")
    @Column(name = "login")
    private String login;
    @NotEmpty(message = "Пароль не может быть пустым")
    @Column(name = "password")
    private String password;

    @Column(name="role")
    private String role;
    @ManyToMany()
    @JoinTable(name="tour_basket", joinColumns = @JoinColumn(name="person_id"), inverseJoinColumns = @JoinColumn(name="tour_id"))
    private List<Tour> tourList;

    @OneToMany(mappedBy = "person", fetch = FetchType.EAGER)
    private List<Order> orderList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return id == person.id && Objects.equals(login, person.login) && Objects.equals(password, person.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, login, password);
    }


}


