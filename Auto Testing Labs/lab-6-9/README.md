# Auto Testing — Labs 6-9 (единый проект)

Один Maven проект, все 4 лабы в одном репозитории.

## Структура

```
lab-6-9/
├── pom.xml
├── testng.xml                      ← Запуск BDD (lab 6,7,8) + API (lab 9)
├── src/test/
│   ├── java/com/stv/
│   │   ├── bdd/
│   │   │   ├── TestRunner.java     ← Cucumber TestNG Runner (lab 6,7,8)
│   │   │   ├── lab6/               ← LoginSteps, RegistrationSteps
│   │   │   ├── lab7/               ← ZipCodeBugSteps
│   │   │   └── lab8/               ← CustomerLookupSteps
│   │   ├── api/
│   │   │   ├── core/               ← ApiBaseTest
│   │   │   ├── domain/             ← Post, Comment, TestData
│   │   │   └── tests/              ← PostTests, CommentTests, SmokeTests
│   │   ├── design/                 ← Shared Page Object (MainPage, Page)
│   │   ├── factory/                ← Factory Pattern (FactoryMainPage etc.)
│   │   └── framework/              ← Driver, URLs, Waiters
│   └── resources/
│       ├── features/
│       │   ├── login.feature               ← Lab 6
│       │   ├── registration.feature         ← Lab 6
│       │   ├── zipcode_validation.feature    ← Lab 7
│       │   └── customer_lookup.feature      ← Lab 8
│       └── log4j2.xml                       ← Lab 8 logger
```

## Как запустить

```bash
cd "Auto Testing Labs/lab-6-9"
mvn clean test
```

## Лабы

| Лаба | Описание |
|------|----------|
| 6 | BDD Cucumber: login + registration (ParaBank) |
| 7 | Bug PB-BUG-001: Zip Code field validation |
| 8 | BDD Individual: Customer Lookup form (Log4j) |
| 9 | API: REST Assured + JSONPlaceholder (16 тестов) |

## Shared framework

Все Selenium лабы (6-8) используют один framework:
- `design/` — Page Object Model
- `factory/` — Factory pattern
- `framework/` — WebDriver, URLs, Waiters
