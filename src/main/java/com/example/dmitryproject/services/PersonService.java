package com.example.dmitryproject.services;

import com.example.dmitryproject.enumm.Role;
import com.example.dmitryproject.models.Person;
import com.example.dmitryproject.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PersonService {
    private final PersonRepository personRepository;

    private final PasswordEncoder passwordEncoder;
    @Autowired
    public PersonService(PersonRepository personRepository, PasswordEncoder passwordEncoder) {
        this.personRepository = personRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public Person findByLogin (Person person){
        Optional<Person> person_db = personRepository.findByLogin(person.getLogin());
        return person_db.orElse(null);
    }

    @Transactional
    public void register(Person person){
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        //по-умолчанию устанавливаем роль юзер
        person.setRole("ROLE_USER");
        personRepository.save(person);
    }

//    public List<Person> getAllPerson(){
//        List<Person> personList = personRepository.findAll();
//        return personList;
//    }

    public List<Person> getAllPerson(){
        return personRepository.findAll();
    }

    public Person infoPerson(int id){
        Optional<Person> optionalPerson = personRepository.findById(id);
        return optionalPerson.orElse(null);
    }
    //когда мы передаём ID, spring Data JPA сам понимает, что мы хотим обновить информацию о продукте а не сохранить новый
    @Transactional
    public void editPerson(int id, Person person){
        person.setId(id);
        personRepository.save(person);
    }
}
