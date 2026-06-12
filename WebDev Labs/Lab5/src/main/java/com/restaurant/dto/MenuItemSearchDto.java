package com.restaurant.dto;

import jakarta.validation.constraints.Size;

public class MenuItemSearchDto {
    @Size(max = 100)
    private String name;

    @Size(max = 50)
    private String category;

    private Long restaurantId;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public Long getRestaurantId() { return restaurantId; }
    public void setRestaurantId(Long restaurantId) { this.restaurantId = restaurantId; }
}
