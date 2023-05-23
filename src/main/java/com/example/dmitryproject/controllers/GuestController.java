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
    public String tourSearch(@RequestParam("search") String search,
                             @RequestParam("on") String on,
                             @RequestParam("off") String off,
                             @RequestParam(value = "price", required = false, defaultValue = "") String price,
                             @RequestParam(value = "location", required = false, defaultValue = "") String location,
                             Model model){
        model.addAttribute("tours", tourService.infoAllTours());

        if(!on.isEmpty() & !off.isEmpty()){
            if(!price.isEmpty()){//Если радиокнопка сортировки ("по возрастанию цены"/по "убыванию цены") не пустая
                if(price.equals("sorted_by_ascending_price")) {//если выбрано значение "сортировка по возрастанию цены
                    if (!location.isEmpty()) {//Если радиокнопка "Направление тура" не пустое значение
                        if (location.equals("Altai")) {//Если выбрана категория Алтай(id=1, смотрим id по БД категорий), то:
                            //Кладем в модель "search_product" и в качестве значения кладем туда то, что возвращает соответствующий метод(в метод передаём то, что пришло с формы, дополнительно поисковый запрос приводим к нижнему регистру, значения полей Цена "От"/"До" приводим ко флоат
                            model.addAttribute("search_product", tourRepository.findByTitleAndLocationOrderByPriceAsc(search.toLowerCase(), Float.parseFloat(on), Float.parseFloat(off), 1));
                        } else if (location.equals("Karelia")) {
                            model.addAttribute("search_product", tourRepository.findByTitleAndLocationOrderByPriceAsc(search.toLowerCase(), Float.parseFloat(on), Float.parseFloat(off), 2));
                        } else if (location.equals("Crimea")) {
                            model.addAttribute("search_product", tourRepository.findByTitleAndLocationOrderByPriceAsc(search.toLowerCase(), Float.parseFloat(on), Float.parseFloat(off), 3));
                        }
                    } else { //Если категория товара не выбрана, то вызываем соответствующий метод для поиска по наименованию и сортировке по возрастанию цены
                        model.addAttribute("search_product", tourRepository.findByTitleOrderByPriceAsc(search.toLowerCase(), Float.parseFloat(on), Float.parseFloat(off)));
                    }
                    //Если установлен сорт по уменьшению цены, то алгоритм как выше, только используем методы с фильтром от высокой к низкой цене
                } else if(price.equals("sorted_by_descending_price")){
                    if(!location.isEmpty()){
                        System.out.println(location);
                        if(location.equals("Altai")){
                            model.addAttribute("search_product", tourRepository.findByTitleAndLocationOrderByPriceDesc(search.toLowerCase(), Float.parseFloat(on), Float.parseFloat(off), 1));
                        }else if (location.equals("Karelia")) {
                            model.addAttribute("search_product", tourRepository.findByTitleAndLocationOrderByPriceDesc(search.toLowerCase(), Float.parseFloat(on), Float.parseFloat(off), 2));
                        } else if (location.equals("Crimea")) {
                            model.addAttribute("search_product", tourRepository.findByTitleAndLocationOrderByPriceDesc(search.toLowerCase(), Float.parseFloat(on), Float.parseFloat(off), 3));
                        }
                    }  else {
                        model.addAttribute("search_product", tourRepository.findByTitleOrderByPriceDesc(search.toLowerCase(), Float.parseFloat(on), Float.parseFloat(off)));
                    }
                }
            } else {//Если радиокнопка сортировки цены не указана,
                //ищем по наименованию и диапазону указанной цены
                model.addAttribute("search_product", tourRepository.findByTitleAndPriceGreaterThanEqualAndPriceLessThanEqual(search.toLowerCase(), Float.parseFloat(on), Float.parseFloat(off)));
            }
        } else {//Если поля Цена "От"/"До" не заполнены, то ищем по наименованию
            model.addAttribute("search_product", tourRepository.findByTitleContainingIgnoreCase(search));
        }
        //Кладём в модель обратно полученные значения с формы для того, чтобы после отправки формы (произойдёт перезагрузка
        // страницы) отправить в форму эти значения для автозаполнения полей по ключу "attributeName" ("value_search" и тд)
        model.addAttribute("value_search", search);
        model.addAttribute("value_price_on", on);
        model.addAttribute("value_price_off", off);
        return "/guest/guest";
    }
}
