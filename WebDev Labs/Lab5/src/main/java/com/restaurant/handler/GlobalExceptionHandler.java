package com.restaurant.handler;

import com.restaurant.exception.DuplicateResourceException;
import com.restaurant.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    public String handleNotFound(ResourceNotFoundException ex, Model model) {
        log.warn("Resource not found: {} (id={})", ex.getResourceName(), ex.getResourceId());
        model.addAttribute("errorTitle", "Не найдено");
        model.addAttribute("errorMessage", ex.getMessage());
        return "error";
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public String handleDuplicate(DuplicateResourceException ex, Model model) {
        log.warn("Duplicate resource: {} ({}={})", ex.getResourceName(), ex.getField(), ex.getValue());
        model.addAttribute("errorTitle", "Дубликат");
        model.addAttribute("errorMessage", ex.getMessage());
        return "error";
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public String handle404(NoResourceFoundException ex, Model model) {
        log.warn("404: {}", ex.getMessage());
        model.addAttribute("errorTitle", "404 — Страница не найдена");
        model.addAttribute("errorMessage", "Запрашиваемая страница не существует.");
        return "error";
    }

    @ExceptionHandler(Exception.class)
    public String handleGeneral(Exception ex, Model model) {
        log.error("Unhandled exception: {}", ex.getMessage(), ex);
        model.addAttribute("errorTitle", "500 — Ошибка сервера");
        model.addAttribute("errorMessage", "Произошла внутренняя ошибка. Попробуйте позже.");
        return "error";
    }
}
