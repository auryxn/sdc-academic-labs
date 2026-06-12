package com.restaurant.dto;

import jakarta.validation.constraints.Size;

public class RestaurantSearchDto {
    @Size(max = 100)
    private String name;

    @Size(max = 50)
    private String cuisine;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCuisine() { return cuisine; }
    public void setCuisine(String cuisine) { this.cuisine = cuisine; }
}
