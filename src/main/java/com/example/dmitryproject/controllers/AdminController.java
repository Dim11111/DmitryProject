package com.example.dmitryproject.controllers;

import com.example.dmitryproject.enumm.Role;
import com.example.dmitryproject.enumm.Status;
import com.example.dmitryproject.models.*;
import com.example.dmitryproject.repositories.LocationRepository;
import com.example.dmitryproject.repositories.OrderRepository;
import com.example.dmitryproject.services.OrderService;
import com.example.dmitryproject.services.PersonService;
import com.example.dmitryproject.services.TourService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Controller
public class AdminController {
    private final TourService tourService;
    private final LocationRepository locationRepository;

    private final OrderRepository orderRepository;
    private final OrderService orderService;

    private final PersonService personService;



    @Value("${upload.path}")
    private String uploadPath;


    public AdminController(TourService tourService, LocationRepository locationRepository, OrderRepository orderRepository, OrderService orderService, PersonService personService) {
        this.tourService = tourService;
        this.locationRepository = locationRepository;
        this.orderRepository = orderRepository;
        this.orderService = orderService;
        this.personService = personService;
    }

    @GetMapping("/admin") //это в адресной строке
    public String admin(Model model){
        model.addAttribute("tours", tourService.infoAllTours());
        List<Order> orderList = orderRepository.findAll();
        model.addAttribute("orders", orderList);
        return "admin/admin"; //это адрес формы
    }
    @GetMapping("/admin/tour/add")
    public String addTour(Model model){
        model.addAttribute("tour", new Tour());
        model.addAttribute("location", locationRepository.findAll());
        return "admin/addTour";
    }

    @PostMapping("/admin/tour/add")
    public String addTour(@ModelAttribute("tour") @Valid Tour tour, BindingResult bindingResult, Model model, @RequestParam("location") int location, @RequestParam("file_one")MultipartFile file_one, @RequestParam("file_two")MultipartFile file_two, @RequestParam("file_three")MultipartFile file_three) throws IOException{
        Location location_db = (Location) locationRepository.findById(location).orElseThrow();
        if(bindingResult.hasErrors()){
            model.addAttribute("location", locationRepository.findAll());
            return "admin/addTour";
        }
        if(file_one !=null){
            File uploadDir = new File(uploadPath);
            if(!uploadDir.exists()){
                uploadDir.mkdir();
            }
            String uuidFile = UUID.randomUUID().toString();
            String resultFileName = uuidFile + "." + file_one.getOriginalFilename();
            file_one.transferTo(new File(uploadPath + "/" + resultFileName));
            Image image = new Image();
            image.setTour(tour);
            image.setFileName(resultFileName);
            tour.addImageToTour(image);
        }
        if(file_two !=null){
            File uploadDir = new File(uploadPath);
            if(!uploadDir.exists()){
                uploadDir.mkdir();
            }
            String uuidFile = UUID.randomUUID().toString();
            String resultFileName = uuidFile + "." + file_two.getOriginalFilename();
            file_two.transferTo(new File(uploadPath + "/" + resultFileName));
            Image image = new Image();
            image.setTour(tour);
            image.setFileName(resultFileName);
            tour.addImageToTour(image);
        }
        if(file_three !=null){
            File uploadDir = new File(uploadPath);
            if(!uploadDir.exists()){
                uploadDir.mkdir();
            }
            String uuidFile = UUID.randomUUID().toString();
            String resultFileName = uuidFile + "." + file_three.getOriginalFilename();
            file_three.transferTo(new File(uploadPath + "/" + resultFileName));
            Image image = new Image();
            image.setTour(tour);
            image.setFileName(resultFileName);
            tour.addImageToTour(image);
        }
        tourService.saveTour(tour, location_db);
        return "redirect:/admin";
    }

    @GetMapping("/admin/tour/edit/{id}")
    public String editTour(@PathVariable("id") int id, Model model){
        model.addAttribute("location", locationRepository.findAll());
        model.addAttribute("tour", tourService.infoTour(id));
        return "admin/editTour";
    }

    @PostMapping("/admin/tour/edit/{id}")
    public String editTour(@ModelAttribute("tour") @Valid Tour tour, BindingResult bindingResult, @PathVariable("id") int id, Model model){
        if(bindingResult.hasErrors()){
            model.addAttribute("location", locationRepository.findAll());
            return "admin/editTour";
        }
        tourService.editTour(id, tour);
        return "redirect:/admin";
    }

    @GetMapping("/admin/tour/delete/{id}")
    public String deleteTour(@PathVariable("id")int id){
        tourService.deleteTour(id);
        return "redirect:/admin";
    }

    @GetMapping("/admin/orders")
    public String getAllOrders(Model model){
        model.addAttribute("orders", orderService.getAllOrders());
        return "admin/allOrders";
    }

    @GetMapping("/admin/order/edit/{id}")
    public String editOrder(@PathVariable("id") int id, Model model){
        model.addAttribute("order", orderService.infoOrder(id));
        model.addAttribute("status", Status.values());
        return "admin/editOrder";
    }

//    @PostMapping("/admin/order/edit/{id}")
//    public String editOrder(@ModelAttribute("order") Order order, @ModelAttribute("status") Status status, @PathVariable("id") int id, Model model){
//        order.setStatus(status);
//        System.out.println(order.getId());
//        System.out.println(order.getNumber());
//        System.out.println(order.getTour());
//        System.out.println(order.getPerson());
//        System.out.println(order.getCount());
//        System.out.println(order.getPrice());
//        System.out.println(order.getStatus());
//        orderService.editOrder(id, order);
//        return "admin/admin";
//    }

    @PostMapping("/admin/order/edit/{id}")
    public String editOrder(@ModelAttribute("status") Status status, @PathVariable("id") int id) {
        Order order = orderService.infoOrder(id); //снова получаем объект заказа из БД
        order.setStatus(status); //меняем статус на выбранный в селекте
        orderService.editOrder(id, order); //сохраняем изменения в базу данных
        return "redirect:/admin";
    }

    @GetMapping("/admin/persons")
    public String getAllUsers(Model model){
        model.addAttribute("persons", personService.getAllPerson());
        return "/admin/allPerson";
    }

    @GetMapping("/admin/person/edit/{id}")
    public String editPerson(@PathVariable("id") int id, Model model){
        model.addAttribute("person", personService.infoPerson(id));
        model.addAttribute("roles", Role.values());
        return "/admin/editPerson";
    }

    @PostMapping("/admin/person/edit/{id}")
    public String editPerson(@PathVariable("id")int id, @ModelAttribute("role") String role){
        Person person = personService.infoPerson(id);
        person.setRole(role);
        personService.editPerson(id, person);
        return "redirect:/admin/persons";
    }

    @PostMapping("/admin/orders/search")
    public String searchOrders(@RequestParam("search") String search, Model model){
        model.addAttribute("orders", orderService.getAllOrders());
        model.addAttribute("search_orders", orderRepository.findOrderByNumberIgnoreCase(search));
        //Кладём в модель обратно полученные значения с формы для того, чтобы после отправки формы (произойдёт перезагрузка
        // страницы) отправить в инпут ранее введённое значение
        model.addAttribute("value_search", search);
        return "/admin/allOrders";
    }
}
