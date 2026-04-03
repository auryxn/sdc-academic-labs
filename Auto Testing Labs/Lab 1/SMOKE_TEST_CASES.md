# Lab 1: Smoke Test Cases for ParaBank Login Section (Revised)

**Task:** Write functional test cases (Smoke Test) to cover the "Customer Login" section.
**URL:** https://parabank.parasoft.com/parabank/index.htm
**Status:** Revised for Smoke Only focus.

---

## 1. Test Case: Successful Login with Valid Credentials (Critical Path)
- **Goal:** Verify that a registered user can successfully log into the system and reach the account dashboard.
- **Pre-conditions:** User "john" is registered with password "demo".
- **Steps:**
    1. Open the browser and navigate to the ParaBank home page.
    2. Enter "john" in the "Username" field.
    3. Enter "demo" in the "Password" field.
    4. Click the "Log In" button.
- **Expected Result:** The user is redirected to the "Account Overview" page. A "Welcome" message with the user's name is displayed. The system is considered "up" and functional for basic operations.

## 2. Test Case: Navigation to Registration Page
- **Goal:** Verify that the "Register" link correctly redirects to the registration form.
- **Steps:**
    1. Open the browser and navigate to the ParaBank home page.
    2. Click on the "Register" link below the login button.
- **Expected Result:** The user is redirected to the "Signing up is easy!" page.

## 3. Test Case: Navigation to Forgot Login Info Page
- **Goal:** Verify that the "Forgot login info?" link works correctly.
- **Steps:**
    1. Open the browser and navigate to the ParaBank home page.
    2. Click on the "Forgot login info?" link.
- **Expected Result:** The user is redirected to the "Customer Look-up" page.

---
**Note:** Negative test cases (incorrect passwords, empty fields) have been removed from this suite to strictly adhere to Smoke Testing principles (verifying build stability and critical path functionality).
