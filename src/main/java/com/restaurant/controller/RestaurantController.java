package com.restaurant.controller;

import com.restaurant.dto.RestaurantCreateDto;
import com.restaurant.dto.RestaurantSearchDto;
import com.restaurant.dto.RestaurantViewDto;
import com.restaurant.service.RestaurantService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/restaurants")
public class RestaurantController {

    private static final Logger log = LoggerFactory.getLogger(RestaurantController.class);

    private final RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @GetMapping
    public String list(@ModelAttribute("search") RestaurantSearchDto search, Model model) {
        log.info("GET /restaurants with search: name='{}', cuisine='{}'", search.getName(), search.getCuisine());
        List<RestaurantViewDto> restaurants = restaurantService.search(search);
        model.addAttribute("restaurants", restaurants);
        model.addAttribute("search", search);
        return "restaurant-list";
    }

    @GetMapping("/{id}")
    public String view(@PathVariable Long id, Model model) {
        log.info("GET /restaurants/{}", id);
        RestaurantViewDto r = restaurantService.findById(id);
        model.addAttribute("restaurant", r);
        return "restaurant-view";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        log.info("GET /restaurants/new");
        model.addAttribute("restaurant", new RestaurantCreateDto());
        return "restaurant-form";
    }

    @PostMapping("/new")
    public String create(@Valid @ModelAttribute("restaurant") RestaurantCreateDto dto,
                         BindingResult result, Model model) {
        if (result.hasErrors()) {
            log.warn("Restaurant creation validation failed: {}", result.getAllErrors());
            return "restaurant-form";
        }
        log.info("POST /restaurants/new -> creating '{}'", dto.getName());
        RestaurantViewDto saved = restaurantService.create(dto);
        return "redirect:/restaurants/" + saved.getId();
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        log.info("GET /restaurants/{}/edit", id);
        RestaurantViewDto r = restaurantService.findById(id);
        RestaurantCreateDto dto = new RestaurantCreateDto();
        dto.setName(r.getName());
        dto.setAddress(r.getAddress());
        dto.setCuisine(r.getCuisine());
        model.addAttribute("restaurant", dto);
        model.addAttribute("restaurantId", id);
        return "restaurant-form";
    }

    @PostMapping("/{id}/edit")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("restaurant") RestaurantCreateDto dto,
                         BindingResult result, Model model) {
        if (result.hasErrors()) {
            log.warn("Restaurant {} update validation failed: {}", id, result.getAllErrors());
            model.addAttribute("restaurantId", id);
            return "restaurant-form";
        }
        log.info("POST /restaurants/{}/edit -> updating", id);
        restaurantService.update(id, dto);
        return "redirect:/restaurants/" + id;
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        log.info("POST /restaurants/{}/delete", id);
        restaurantService.delete(id);
        return "redirect:/restaurants";
    }
}
