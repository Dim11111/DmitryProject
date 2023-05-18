package com.example.dmitryproject.controllers;

import com.example.dmitryproject.models.Image;
import com.example.dmitryproject.models.Location;
import com.example.dmitryproject.models.Tour;
import com.example.dmitryproject.repositories.LocationRepository;
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
import java.util.UUID;

@Controller
public class AdminController {
    private final TourService tourService;
    private final LocationRepository locationRepository;

    @Value("${upload.path}")
    private String uploadPath;


    public AdminController(TourService tourService, LocationRepository locationRepository) {
        this.tourService = tourService;
        this.locationRepository = locationRepository;
    }

    @GetMapping("/admin") //это в адресной строке
    public String admin(Model model){
        model.addAttribute("tours", tourService.infoAllTours());
        return "/admin/admin"; //это адрес формы
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

}
