# Lab 8 — BDD Individual Task

## Scenarios

### a) Bill Pay (Individual case)
**Feature file:** `bill_pay.feature`
User logs into ParaBank with valid credentials, clicks "Bill Pay", fills out payee information (name, address, account, amount), submits, and verifies the success message.

### b) Loan Request (Scenario Outline)
**Feature file:** `loan_request.feature`
User applies for different loan amounts (1000, 5000, 25000, 100000) using Scenario Outline with Examples table.

## Bonus Features
- **Log4j 2** — logging in all step definitions
- **ExtentReports** — HTML reporter with charts
- **Custom TestRunner** — @BeforeSuite / @AfterSuite for setup/teardown

## How to Run
```bash
cd "Auto Testing Labs/lab 8"
mvn clean test
```
Report will be generated at `target/ExtentReport.html`.
