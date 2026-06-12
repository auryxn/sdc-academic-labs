package com.restaurant.exception;

public class ResourceNotFoundException extends RuntimeException {
    private Long resourceId;

    public ResourceNotFoundException(String message, Long resourceId) {
        super(message);
        this.resourceId = resourceId;
    }

    public Long getResourceId() {
        return resourceId;
    }
}
