# Lab 5: Logging & Exception Handling

## Цель
Добавить логирование и глобальную обработку ошибок в Spring Boot приложение.

## Что сделано

### 1. Логирование (SLF4J + Logback)
- Все контроллеры и сервисы логируют ключевые операции
- `log.info()` — для основных действий (создание, обновление, удаление)
- `log.debug()` — для детального трейсинга (поиск, списки)
- `log.warn()` — для ошибок валидации и 404
- `log.error()` — для необработанных исключений

### 2. Кастомные исключения
- `ResourceNotFoundException` — ресурс не найден (404)
- `DuplicateResourceException` — дубликат ресурса

### 3. Глобальный обработчик ошибок
- `GlobalExceptionHandler` с `@ControllerAdvice`
- Отлавливает все исключения и показывает `error.html`
- Страница 404 для несуществующих URL
- Страница 500 для внутренних ошибок

### 4. Настройки логирования
- `application.properties`:
  - `com.restaurant` — DEBUG уровень
  - `org.springframework` — WARN
  - Формат: дата, тред, уровень, логгер, сообщение

## Структура
```
exception/
├── ResourceNotFoundException.java
└── DuplicateResourceException.java
handler/
└── GlobalExceptionHandler.java
controller/
├── HomeController.java       — +slf4j
├── RestaurantController.java  — +slf4j
└── MenuItemController.java    — +slf4j
service/
├── RestaurantService.java     — +slf4j + кастомные исключения
└── MenuItemService.java       — +slf4j + кастомные исключения
resources/
├── application.properties     — +настройки логов
└── templates/
    └── error.html             — новая страница ошибок
```
