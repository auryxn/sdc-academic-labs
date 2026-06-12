# Lab 5: Logging, Error Handling, AOP

## Structure

```
controller/
  HomeController.java         — MVC /
  MenuItemController.java     — MVC CRUD menu items
  RestaurantController.java   — MVC CRUD restaurants
  MenuItemApiController.java  — REST API /api/menu-items
service/
  MenuItemService.java        — business logic + logging
  RestaurantService.java      — business logic + logging
repository/
  MenuItemRepository.java     — JPA
  RestaurantRepository.java   — JPA
exception/
  ResourceNotFoundException   — custom 404 exception
  DuplicateResourceException  — custom duplicate exception
  ErrorResponse               — structured JSON error
handler/
  GlobalExceptionHandler      — @ControllerAdvice (404/500)
aspect/
  LoggingAspect               — @Aspect for controller & service
model/
  MenuItem.java / Restaurant.java
dto/
  *CreateDto / *ViewDto / *SearchDto
```

## Criteria

- SLF4J + Logback logging (INFO/DEBUG/WARN/ERROR)
- Custom exceptions (ResourceNotFoundException, DuplicateResourceException)
- @ControllerAdvice with correct HTTP statuses (404/500)
- JSON errors for REST API, HTML errors for MVC
- @Aspect for logging controller entry/exit + service timing
- @EnableAspectJAutoProxy
- 3-layer architecture: controller / service / repository
- Separate DTOs for create, view, search
- Server-side validation with @Valid + BindingResult
- Validation errors shown on Thymeleaf forms
