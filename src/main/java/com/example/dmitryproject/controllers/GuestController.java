package com.example.dmitryproject.controllers;

import com.example.dmitryproject.repositories.TourRepository;
import com.example.dmitryproject.services.TourService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/guest")
public class GuestController {

    private final TourService tourService;
    private final TourRepository tourRepository;

    public GuestController(TourService tourService, TourRepository tourRepository) {
        this.tourService = tourService;
        this.tourRepository = tourRepository;
    }
    @GetMapping("/tours")
    public String getAllTours(Model model){
        model.addAttribute("tours", tourService.infoAllTours());
        return "guest/guest";
    }

    @GetMapping("/tour/{id}")
    public String infoTour(@PathVariable("id") int id, Model model){
        model.addAttribute("tour", tourService.infoTour(id));
        return "/guest/tour";
    }

    @PostMapping("/guest/search")
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
        return "/guest/guest";
    }
}
