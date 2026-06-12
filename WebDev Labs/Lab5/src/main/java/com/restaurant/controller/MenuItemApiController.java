package com.restaurant.controller;

import com.restaurant.dto.*;
import com.restaurant.exception.ResourceNotFoundException;
import com.restaurant.service.MenuItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menu-items")
public class MenuItemApiController {

    private final MenuItemService menuItemService;

    public MenuItemApiController(MenuItemService menuItemService) {
        this.menuItemService = menuItemService;
    }

    @GetMapping
    public List<MenuItemViewDto> getAll() {
        return menuItemService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MenuItemViewDto> getById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(menuItemService.findById(id));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<MenuItemViewDto> create(@RequestBody MenuItemCreateDto dto) {
        MenuItemViewDto saved = menuItemService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            menuItemService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
