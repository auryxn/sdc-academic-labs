package com.restaurant.controller;

import com.restaurant.dto.*;
import com.restaurant.service.MenuItemService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/menu-items")
public class MenuItemController {

    private static final Logger log = LoggerFactory.getLogger(MenuItemController.class);

    private final MenuItemService menuItemService;

    public MenuItemController(MenuItemService menuItemService) {
        this.menuItemService = menuItemService;
    }

    @GetMapping
    public String list(@ModelAttribute("search") MenuItemSearchDto search, Model model) {
        log.info("GET /menu-items with search: name='{}', category='{}'", search.getName(), search.getCategory());
        List<MenuItemViewDto> items = menuItemService.search(search);
        model.addAttribute("items", items);
        model.addAttribute("search", search);
        return "menuitem-list";
    }

    @GetMapping("/{id}")
    public String view(@PathVariable Long id, Model model) {
        log.info("GET /menu-items/{}", id);
        MenuItemViewDto item = menuItemService.findById(id);
        model.addAttribute("item", item);
        return "menuitem-view";
    }

    @GetMapping("/new")
    public String newForm(@RequestParam(required = false) Long restaurantId, Model model) {
        log.info("GET /menu-items/new (restaurantId={})", restaurantId);
        MenuItemCreateDto dto = new MenuItemCreateDto();
        dto.setRestaurantId(restaurantId);
        model.addAttribute("item", dto);
        return "menuitem-form";
    }

    @PostMapping("/new")
    public String create(@Valid @ModelAttribute("item") MenuItemCreateDto dto,
                         BindingResult result, Model model) {
        if (result.hasErrors()) {
            log.warn("Menu item creation validation failed: {}", result.getAllErrors());
            return "menuitem-form";
        }
        log.info("POST /menu-items/new -> creating '{}'", dto.getName());
        MenuItemViewDto saved = menuItemService.create(dto);
        log.info("Redirecting to /menu-items/{}", saved.getId());
        return "redirect:/menu-items/" + saved.getId();
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        log.info("GET /menu-items/{}/edit", id);
        MenuItemViewDto mi = menuItemService.findById(id);
        MenuItemCreateDto dto = new MenuItemCreateDto();
        dto.setName(mi.getName());
        dto.setDescription(mi.getDescription());
        dto.setPrice(mi.getPrice());
        dto.setCategory(mi.getCategory());
        dto.setRestaurantId(mi.getRestaurantId());
        model.addAttribute("item", dto);
        model.addAttribute("itemId", id);
        return "menuitem-form";
    }

    @PostMapping("/{id}/edit")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("item") MenuItemCreateDto dto,
                         BindingResult result, Model model) {
        if (result.hasErrors()) {
            log.warn("Menu item {} update validation failed: {}", id, result.getAllErrors());
            model.addAttribute("itemId", id);
            return "menuitem-form";
        }
        log.info("POST /menu-items/{}/edit -> updating", id);
        menuItemService.update(id, dto);
        return "redirect:/menu-items/" + id;
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        log.info("POST /menu-items/{}/delete", id);
        menuItemService.delete(id);
        return "redirect:/menu-items";
    }
}
