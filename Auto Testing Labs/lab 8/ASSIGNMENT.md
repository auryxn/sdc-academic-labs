# Task #8 — Framework – BDD – Individual task

**Given:** Framework has been set up, factory package has been added, and appropriate factory PO classes have been implemented, BDD package has been created

**Required:**

a) Implement a cucumber scenario in compliance with the user case given by the teacher.

b) Create a scenario Outline to cover any functional feature that you wish

**Note#1:** the final score will be set after the test demonstration

**Note#2:** if you wish to increase your final score, you can add logger, runner, reporter to your framework

## Implementation

### a) Individual scenario — ParaBank Bill Pay
User logs in, navigates to Bill Pay, enters valid payee details, and verifies the payment confirmation.

**Feature:** `bill_pay.feature`
**Steps:** `BillPaySteps.java`

### b) Scenario Outline — Loan Request with multiple data sets
User applies for different loan amounts and verifies the system's response.

**Feature:** `loan_request.feature`
**Steps:** `LoanRequestSteps.java`

### Bonus — Logger + Extent Reporter
- Log4j 2 logger integrated in all step classes
- ExtentSparkReporter generates HTML report after test run
- Custom TestRunner with beforeSuite/afterSuite hooks
