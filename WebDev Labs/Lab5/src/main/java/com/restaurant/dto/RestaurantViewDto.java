package com.restaurant.dto;

import java.util.List;

public class RestaurantViewDto {
    private Long id;
    private String name;
    private String address;
    private String cuisine;
    private List<MenuItemViewDto> menuItems;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getCuisine() { return cuisine; }
    public void setCuisine(String cuisine) { this.cuisine = cuisine; }

    public List<MenuItemViewDto> getMenuItems() { return menuItems; }
    public void setMenuItems(List<MenuItemViewDto> menuItems) { this.menuItems = menuItems; }
}
