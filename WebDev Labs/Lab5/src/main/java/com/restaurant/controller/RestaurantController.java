package com.restaurant.controller;

import com.restaurant.dto.*;
import com.restaurant.service.RestaurantService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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
        log.info("GET /restaurants");
        model.addAttribute("restaurants", restaurantService.search(search));
        model.addAttribute("search", search);
        return "restaurant-list";
    }

    @GetMapping("/{id}")
    public String view(@PathVariable Long id, Model model) {
        log.info("GET /restaurants/{}", id);
        model.addAttribute("restaurant", restaurantService.findById(id));
        return "restaurant-view";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("restaurant", new RestaurantCreateDto());
        return "restaurant-form";
    }

    @PostMapping("/new")
    public String create(@Valid @ModelAttribute("restaurant") RestaurantCreateDto dto,
                         BindingResult result, Model model) {
        if (result.hasErrors()) {
            log.warn("Validation errors: {}", result.getAllErrors());
            return "restaurant-form";
        }
        log.info("Creating restaurant: {}", dto.getName());
        RestaurantViewDto saved = restaurantService.create(dto);
        return "redirect:/restaurants/" + saved.getId();
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
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
            log.warn("Update validation errors for {}: {}", id, result.getAllErrors());
            model.addAttribute("restaurantId", id);
            return "restaurant-form";
        }
        log.info("Updating restaurant {}", id);
        restaurantService.update(id, dto);
        return "redirect:/restaurants/" + id;
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        log.info("Deleting restaurant {}", id);
        restaurantService.delete(id);
        return "redirect:/restaurants";
    }
}
