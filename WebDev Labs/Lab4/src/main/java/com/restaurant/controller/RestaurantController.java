package com.restaurant.controller;

import com.restaurant.dto.RestaurantCreateDto;
import com.restaurant.dto.RestaurantSearchDto;
import com.restaurant.dto.RestaurantViewDto;
import com.restaurant.service.RestaurantService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/restaurants")
public class RestaurantController {

    private final RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @GetMapping
    public String list(@ModelAttribute("search") RestaurantSearchDto search, Model model) {
        List<RestaurantViewDto> restaurants = restaurantService.search(search);
        model.addAttribute("restaurants", restaurants);
        model.addAttribute("search", search);
        return "restaurant-list";
    }

    @GetMapping("/{id}")
    public String view(@PathVariable Long id, Model model) {
        RestaurantViewDto r = restaurantService.findById(id);
        model.addAttribute("restaurant", r);
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
            return "restaurant-form";
        }
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
            model.addAttribute("restaurantId", id);
            return "restaurant-form";
        }
        restaurantService.update(id, dto);
        return "redirect:/restaurants/" + id;
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        restaurantService.delete(id);
        return "redirect:/restaurants";
    }
}
