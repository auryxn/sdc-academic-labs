# Lab 7 — Automated Test for Bug #PB-BUG-001

## Bug Reference
- **ID:** PB-BUG-001
- **Title:** Missing Input Validation for Zip Code Field
- **Severity:** Medium
- **Description:** The "Zip Code" field on the Registration page accepts non-numeric characters (letters, symbols) without any validation error.

## Test Implementation
This test automates the bug reproduction:
1. Navigate to the ParaBank registration page
2. Fill all required fields with valid data
3. Enter "ABCDE" in the Zip Code field
4. Submit the form
5. Assert that a validation error appears

## Expected Behavior
The test **expects** an error message about invalid zip code.
Since the system accepts non-numeric input **without validation**, the test will **fail** — proving the bug exists.

## Structure
```
lab 7/
├── pom.xml                                    # Maven with Selenium + TestNG + Cucumber
├── src/test/java/com/stv/
│   ├── bdd/
│   │   ├── TestRunner.java                    # TestNG Cucumber Runner
│   │   └── stepdefinitions/
│   │       └── ZipCodeBugSteps.java           # Bug reproduction steps
│   ├── factory/factorypages/                  # Page Objects (reused from Lab 5)
│   ├── design/designtests/
│   │   └── BasicTest.java
│   └── framework/core/
└── src/test/resources/features/
    └── zipcode_validation.feature             # BDD scenario for the bug
```

## How to Run
```bash
cd "Auto Testing Labs/lab 7"
mvn clean test
```

## Expected Result
The test **will fail** with the message: "BUG CONFIRMED: Zip Code field accepts non-numeric input without validation."
This confirms the bug is reproducible.
