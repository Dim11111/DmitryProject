package com.example.dmitryproject.services;

import com.example.dmitryproject.models.Person;
import com.example.dmitryproject.repositories.PersonRepository;
import com.example.dmitryproject.security.PersonDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

//3. в сервисом слое создаем метод поиска пользователя по логину, наследуемся от репозитория
// в UserDetailsService есть метод который ищет пользователя по логину
@Service
public class PersonDetailsService implements UserDetailsService {
    private final PersonRepository personRepository;
    @Autowired
    public PersonDetailsService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    //поиск пользователя по логину
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Person> person = personRepository.findByLogin(username);
        //если юзер не найден выбрасываем исключение
        if(person.isEmpty()){
            throw new UsernameNotFoundException("Пользователь не найден");
        }
        //возвращаем объект авторизованного пользователя (внутренний метод get)
        return new PersonDetails(person.get());
    }
}
