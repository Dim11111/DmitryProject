package com.example.dmitryproject.controllers;

import com.example.dmitryproject.enumm.Status;
import com.example.dmitryproject.models.Basket;
import com.example.dmitryproject.models.Order;
import com.example.dmitryproject.models.Person;
import com.example.dmitryproject.models.Tour;
import com.example.dmitryproject.repositories.BasketRepository;
import com.example.dmitryproject.repositories.OrderRepository;
import com.example.dmitryproject.repositories.TourRepository;
import com.example.dmitryproject.security.PersonDetails;
import com.example.dmitryproject.services.PersonService;
import com.example.dmitryproject.services.TourService;
import com.example.dmitryproject.util.PersonValidator;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
public class UserController {

    private final PersonService personService;
    private final PersonValidator personValidator;
    private final TourService tourService;
    private final TourRepository tourRepository;
    private final BasketRepository basketRepository;

    private final OrderRepository orderRepository;

    public UserController(PersonService personService, PersonValidator personValidator, TourService tourService, TourRepository tourRepository, BasketRepository basketRepository, OrderRepository orderRepository) {
        this.personService = personService;
        this.personValidator = personValidator;
        this.tourService = tourService;
        this.tourRepository = tourRepository;
        this.basketRepository = basketRepository;
        this.orderRepository = orderRepository;
    }

    @GetMapping("/authentication")
    public String login(){
        return "authentication";
    }

    @GetMapping("/user")
    public String index(Model model){
        //принимаем объект аутентификации
        //из аутентификации получаем объект аутентифицированного пользователя PersonDetails из сессии
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        String role = personDetails.getPerson().getRole();
        if(role.equals("ROLE_ADMIN")){
            return "redirect:/admin";
        }
        model.addAttribute("tours", tourService.infoAllTours());
        return "/user/user";
        }
        @GetMapping("/registration")
        public String registration(@ModelAttribute ("person") Person person){
            return "/registration";
        }

        @PostMapping("/registration")
        public String registration(@ModelAttribute ("person") @Valid Person person, BindingResult bindingResult){
            personValidator.validate(person, bindingResult);
            if(bindingResult.hasErrors()){
                return "/registration";
            }
            personService.register(person);
            return "/user/user";
        }

        @GetMapping("/user/tours")
        public String getAllTours(Model model){
            model.addAttribute("tours", tourService.infoAllTours());
            return "/user/user";
        }

        @GetMapping("/user/tour/{id}")
        public String infoTour(@PathVariable("id") int id, Model model){
            model.addAttribute("tour", tourService.infoTour(id));
            return "/user/tour";
    }

    @PostMapping ("/user/search")
    public String tourSearch(@RequestParam("search") String search, @RequestParam("on") String on, @RequestParam("off") String off, @RequestParam(value = "price", required = false, defaultValue = "") String price, @RequestParam(value = "location", required = false, defaultValue = "") String location, Model model){
        model.addAttribute("tours", tourService.infoAllTours());

        if(!on.isEmpty() & !off.isEmpty()){
            if(!price.isEmpty()){
                if(price.equals("sorted_by_ascending_price")) {
                    if (!location.isEmpty()) {
                        if (location.equals("altai")) {
                            model.addAttribute("search_product", tourRepository.findByTitleAndLocationOrderByPriceAsc(search.toLowerCase(), Float.parseFloat(on), Float.parseFloat(off), 1));
                        } else if (location.equals("karelia")) {
                            model.addAttribute("search_product", tourRepository.findByTitleAndLocationOrderByPriceAsc(search.toLowerCase(), Float.parseFloat(on), Float.parseFloat(off), 2));
                        } else if (location.equals("crimea")) {
                            model.addAttribute("search_product", tourRepository.findByTitleAndLocationOrderByPriceAsc(search.toLowerCase(), Float.parseFloat(on), Float.parseFloat(off), 3));
                        }
                    } else {
                        model.addAttribute("search_product", tourRepository.findByTitleOrderByPriceAsc(search.toLowerCase(), Float.parseFloat(on), Float.parseFloat(off)));
                    }
                } else if(price.equals("sorted_by_descending_price")){
                    if(!location.isEmpty()){
                        System.out.println(location);
                        if(location.equals("altai")){
                            model.addAttribute("search_product", tourRepository.findByTitleAndLocationOrderByPriceDesc(search.toLowerCase(), Float.parseFloat(on), Float.parseFloat(off), 1));
                        }else if (location.equals("karelia")) {
                            model.addAttribute("search_product", tourRepository.findByTitleAndLocationOrderByPriceDesc(search.toLowerCase(), Float.parseFloat(on), Float.parseFloat(off), 2));
                        } else if (location.equals("crimea")) {
                            model.addAttribute("search_product", tourRepository.findByTitleAndLocationOrderByPriceDesc(search.toLowerCase(), Float.parseFloat(on), Float.parseFloat(off), 3));
                        }
                    }  else {
                        model.addAttribute("search_product", tourRepository.findByTitleOrderByPriceDesc(search.toLowerCase(), Float.parseFloat(on), Float.parseFloat(off)));
                    }
                }
            } else {
                model.addAttribute("search_product", tourRepository.findByTitleAndPriceGreaterThanEqualAndPriceLessThanEqual(search.toLowerCase(), Float.parseFloat(on), Float.parseFloat(off)));
            }
        } else {
            model.addAttribute("search_product", tourRepository.findByTitleContainingIgnoreCase(search));
        }

        model.addAttribute("value_search", search);
        model.addAttribute("value_price_on", on);
        model.addAttribute("value_price_off", off);
        return "/user/user";
    }

    @GetMapping("/basket/add/{id}")
    public String addTourToBasket(@PathVariable("id") int id, Model model){
        Tour tour = tourService.infoTour(id);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        int id_person = personDetails.getPerson().getId();
        Basket basket = new Basket(tour.getId(), id_person);
        basketRepository.save(basket);
        return "redirect:/basket";
    }

    @GetMapping("/basket")
    public String basket(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        int id_person = personDetails.getPerson().getId();
        List<Basket> basketList = basketRepository.findByPersonId(id_person);
        List<Tour> tourList = new ArrayList<>();

        for (Basket basket: basketList){
            tourList.add(tourService.infoTour(basket.getTourId()));
        }

        int price = 0;
        for (Tour tour: tourList){
            price += tour.getPrice();
        }
        model.addAttribute("price", price);
        model.addAttribute("basket_tour", tourList);
        return "/user/basket";
    }

    @GetMapping("/basket/delete/{id}")
    public String deleteFromBasket(@PathVariable ("id") int id, Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        int id_person = personDetails.getPerson().getId();
        List<Basket> basketList = basketRepository.findByPersonId(id_person);
        List<Tour> tourList = new ArrayList<>();

        for (Basket basket: basketList){
            tourList.add(tourService.infoTour(basket.getTourId()));
        }

        basketRepository.deleteBasketByTourId(id);
        return "redirect:/basket";
    }
    @GetMapping("/order/create")
    public String order() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        int id_person = personDetails.getPerson().getId();
        List<Basket> basketList = basketRepository.findByPersonId(id_person);
        List<Tour> tourList = new ArrayList<>();

        for (Basket basket : basketList) {
            tourList.add(tourService.infoTour(basket.getTourId()));
        }

        int price = 0;
        for (Tour tour : tourList) {
            price += tour.getPrice();
        }
        String uuid = UUID.randomUUID().toString();
        for (Tour tour : tourList){
            Order newOrder = new Order(uuid, tour, personDetails.getPerson(), 1, (int) tour.getPrice(), Status.Получен);
            orderRepository.save(newOrder);
            basketRepository.deleteBasketByTourId(tour.getId());
        }
        return "redirect:/orders";
    }

    @GetMapping("/orders")
    public String orderUser(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        List<Order> orderList = orderRepository.findByPerson(personDetails.getPerson());
        model.addAttribute("orders", orderList);
        return "/user/orders";
    }
}