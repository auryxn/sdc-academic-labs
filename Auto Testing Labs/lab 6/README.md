# Lab 6 — BDD (Cucumber)

## Task Description
Implement 2 Cucumber scenarios:
1. **Smoke test**: Successful login with valid credentials (from Lab 1)
2. **Navigation test**: Navigate to Registration page (from Lab 1)

## Structure
```
lab 6/
├── pom.xml                                    # Maven with Selenium + TestNG + Cucumber
├── src/test/java/com/stv/
│   ├── bdd/
│   │   ├── TestRunner.java                    # TestNG Cucumber Runner
│   │   └── stepdefinitions/
│   │       ├── LoginSteps.java                # Login scenario steps
│   │       └── RegistrationSteps.java         # Registration navigation steps
│   ├── factory/factorypages/                  # Page Objects (PO + Factory pattern)
│   │   ├── FactoryPage.java
│   │   ├── FactoryMainPage.java
│   │   └── FactoryRegisterPage.java
│   ├── factory/factorytests/
│   │   └── FactoryMainTest.java
│   ├── design/designtests/
│   │   └── BasicTest.java                     # Browser setup/teardown
│   └── framework/core/
│       ├── drivers/MyDriver.java              # WebDriver manager
│       ├── lib/ParaBankPageURLs.java
│       └── utils/Waiters.java
└── src/test/resources/features/
    ├── login.feature                          # Login BDD scenario
    └── registration.feature                   # Registration BDD scenario
```

## How to Run
```bash
cd "Auto Testing Labs/lab 6"
mvn clean test
```

## Scenarios
1. **Successful Login**: `john` / `demo` → Account Overview page with welcome message
2. **Navigate to Registration**: Click Register link → "Signing up is easy!" title
