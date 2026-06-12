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
public class MenuItemService {

    private static final Logger log = LoggerFactory.getLogger(MenuItemService.class);
    private final MenuItemRepository menuItemRepo;
    private final RestaurantRepository restaurantRepo;

    public MenuItemService(MenuItemRepository menuItemRepo, RestaurantRepository restaurantRepo) {
        this.menuItemRepo = menuItemRepo;
        this.restaurantRepo = restaurantRepo;
    }

    public List<MenuItemViewDto> findAll() {
        log.debug("Fetching all menu items");
        return menuItemRepo.findAll().stream()
                .map(this::toViewDto)
                .collect(Collectors.toList());
    }

    public MenuItemViewDto findById(Long id) {
        log.debug("Finding menu item by id: {}", id);
        MenuItem mi = menuItemRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Menu item not found: " + id, id));
        return toViewDto(mi);
    }

    public List<MenuItemViewDto> findByRestaurant(Long restaurantId) {
        return menuItemRepo.findByRestaurantId(restaurantId).stream()
                .map(this::toViewDto)
                .collect(Collectors.toList());
    }

    public List<MenuItemViewDto> search(MenuItemSearchDto search) {
        String name = search.getName();
        String category = search.getCategory();
        Long restId = search.getRestaurantId();
        boolean hasName = name != null && !name.isBlank();
        boolean hasCategory = category != null && !category.isBlank();

        log.debug("Searching menu items");

        List<MenuItem> results;
        if (hasName && hasCategory && restId != null) {
            results = menuItemRepo.findAll().stream()
                    .filter(m -> m.getName().toLowerCase().contains(name.toLowerCase())
                            && m.getCategory().toLowerCase().contains(category.toLowerCase())
                            && m.getRestaurantId().equals(restId))
                    .collect(Collectors.toList());
        } else if (hasName) {
            results = menuItemRepo.findByNameContainingIgnoreCase(name);
        } else if (hasCategory) {
            results = menuItemRepo.findByCategoryContainingIgnoreCase(category);
        } else if (restId != null) {
            results = menuItemRepo.findByRestaurantId(restId);
        } else {
            results = menuItemRepo.findAll();
        }
        return results.stream().map(this::toViewDto).collect(Collectors.toList());
    }

    @Transactional
    public MenuItemViewDto create(MenuItemCreateDto dto) {
        log.info("Creating menu item: {}", dto.getName());
        Restaurant r = restaurantRepo.findById(dto.getRestaurantId())
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found: " + dto.getRestaurantId(), dto.getRestaurantId()));
        MenuItem mi = new MenuItem(dto.getName(), dto.getDescription(), dto.getPrice(), dto.getCategory());
        mi.setRestaurant(r);
        return toViewDto(menuItemRepo.save(mi));
    }

    @Transactional
    public MenuItemViewDto update(Long id, MenuItemCreateDto dto) {
        log.info("Updating menu item {}", id);
        MenuItem mi = menuItemRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Menu item not found: " + id, id));
        mi.setName(dto.getName());
        mi.setDescription(dto.getDescription());
        mi.setPrice(dto.getPrice());
        mi.setCategory(dto.getCategory());
        if (dto.getRestaurantId() != null && !dto.getRestaurantId().equals(mi.getRestaurantId())) {
            Restaurant r = restaurantRepo.findById(dto.getRestaurantId())
                    .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found: " + dto.getRestaurantId(), dto.getRestaurantId()));
            mi.setRestaurant(r);
        }
        return toViewDto(menuItemRepo.save(mi));
    }

    @Transactional
    public void delete(Long id) {
        log.info("Deleting menu item {}", id);
        if (!menuItemRepo.existsById(id)) {
            throw new ResourceNotFoundException("Menu item not found: " + id, id);
        }
        menuItemRepo.deleteById(id);
    }

    public MenuItemViewDto toViewDto(MenuItem mi) {
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
