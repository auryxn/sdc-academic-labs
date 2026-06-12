package com.restaurant.service;

import com.restaurant.dto.*;
import com.restaurant.exception.ResourceNotFoundException;
import com.restaurant.model.MenuItem;
import com.restaurant.model.Restaurant;
import com.restaurant.repository.MenuItemRepository;
import com.restaurant.repository.RestaurantRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class RestaurantService {

    private static final Logger log = LoggerFactory.getLogger(RestaurantService.class);
    private final RestaurantRepository restaurantRepo;
    private final MenuItemRepository menuItemRepo;

    public RestaurantService(RestaurantRepository restaurantRepo, MenuItemRepository menuItemRepo) {
        this.restaurantRepo = restaurantRepo;
        this.menuItemRepo = menuItemRepo;
    }

    public List<RestaurantViewDto> findAll() {
        log.debug("Fetching all restaurants");
        return restaurantRepo.findAll().stream()
                .map(this::toViewDto)
                .collect(Collectors.toList());
    }

    public RestaurantViewDto findById(Long id) {
        log.debug("Finding restaurant by id: {}", id);
        Restaurant r = restaurantRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found: " + id, id));
        return toViewDto(r);
    }

    public List<RestaurantViewDto> search(RestaurantSearchDto search) {
        String name = search.getName();
        String cuisine = search.getCuisine();
        boolean hasName = name != null && !name.isBlank();
        boolean hasCuisine = cuisine != null && !cuisine.isBlank();

        log.debug("Searching restaurants: name='{}', cuisine='{}'", name, cuisine);

        List<Restaurant> results;
        if (hasName && hasCuisine) {
            results = restaurantRepo.findAll().stream()
                    .filter(r -> r.getName().toLowerCase().contains(name.toLowerCase())
                            && r.getCuisine().toLowerCase().contains(cuisine.toLowerCase()))
                    .collect(Collectors.toList());
        } else if (hasName) {
            results = restaurantRepo.findByNameContainingIgnoreCase(name);
        } else if (hasCuisine) {
            results = restaurantRepo.findByCuisineContainingIgnoreCase(cuisine);
        } else {
            results = restaurantRepo.findAll();
        }
        return results.stream().map(this::toViewDto).collect(Collectors.toList());
    }

    @Transactional
    public RestaurantViewDto create(RestaurantCreateDto dto) {
        log.info("Creating restaurant: {}", dto.getName());
        Restaurant r = new Restaurant(dto.getName(), dto.getAddress(), dto.getCuisine());
        r = restaurantRepo.save(r);
        return toViewDto(r);
    }

    @Transactional
    public RestaurantViewDto update(Long id, RestaurantCreateDto dto) {
        log.info("Updating restaurant {}", id);
        Restaurant r = restaurantRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found: " + id, id));
        r.setName(dto.getName());
        r.setAddress(dto.getAddress());
        r.setCuisine(dto.getCuisine());
        return toViewDto(restaurantRepo.save(r));
    }

    @Transactional
    public void delete(Long id) {
        log.info("Deleting restaurant {}", id);
        if (!restaurantRepo.existsById(id)) {
            throw new ResourceNotFoundException("Restaurant not found: " + id, id);
        }
        restaurantRepo.deleteById(id);
    }

    public RestaurantViewDto toViewDto(Restaurant r) {
        RestaurantViewDto dto = new RestaurantViewDto();
        dto.setId(r.getId());
        dto.setName(r.getName());
        dto.setAddress(r.getAddress());
        dto.setCuisine(r.getCuisine());
        dto.setMenuItems(r.getMenuItems().stream().map(this::toMenuItemViewDto).collect(Collectors.toList()));
        return dto;
    }

    private MenuItemViewDto toMenuItemViewDto(MenuItem mi) {
        MenuItemViewDto dto = new MenuItemViewDto();
        dto.setId(mi.getId());
        dto.setName(mi.getName());
        dto.setDescription(mi.getDescription());
        dto.setPrice(mi.getPrice());
        dto.setCategory(mi.getCategory());
        dto.setRestaurantId(mi.getRestaurantId());
        dto.setRestaurantName(mi.getRestaurant() != null ? mi.getRestaurant().getName() : null);
        return dto;
    }
}
