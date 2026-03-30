# Lab 1: Smoke Test Cases for ParaBank Login Section

**Task:** Write functional test cases (Smoke Test) to cover the "Customer Login" section.
**URL:** https://parabank.parasoft.com/parabank/index.htm
**Deadline:** 2026.03.31

---

## 1. Test Case: Successful Login with Valid Credentials
- **Goal:** Verify that a registered user can successfully log into the system.
- **Pre-conditions:** User "john" is registered with password "demo".
- **Steps:**
    1. Open the browser and navigate to the ParaBank home page.
    2. Enter "john" in the "Username" field.
    3. Enter "demo" in the "Password" field.
    4. Click the "Log In" button.
- **Expected Result:** The user is redirected to the "Account Overview" page. A "Welcome" message with the user's name is displayed.

## 2. Test Case: Login Failure with Incorrect Password
- **Goal:** Verify that the system prevents login with a wrong password.
- **Pre-conditions:** User "john" is registered.
- **Steps:**
    1. Open the browser and navigate to the ParaBank home page.
    2. Enter "john" in the "Username" field.
    3. Enter "wrong_password" in the "Password" field.
    4. Click the "Log In" button.
- **Expected Result:** An error message "The username and password could not be verified" is displayed. The user remains on the login page.

## 3. Test Case: Login Attempt with Empty Fields
- **Goal:** Verify that the system handles empty login credentials.
- **Steps:**
    1. Open the browser and navigate to the ParaBank home page.
    2. Leave both "Username" and "Password" fields empty.
    3. Click the "Log In" button.
- **Expected Result:** An error message "Please enter a username and password" is displayed.

## 4. Test Case: Navigation to Registration Page
- **Goal:** Verify that the "Register" link correctly redirects to the registration form.
- **Steps:**
    1. Open the browser and navigate to the ParaBank home page.
    2. Click on the "Register" link below the login button.
- **Expected Result:** The user is redirected to the "Signing up is easy!" page (Register page).

## 5. Test Case: Navigation to Forgot Login Info Page
- **Goal:** Verify that the "Forgot login info?" link works correctly.
- **Steps:**
    1. Open the browser and navigate to the ParaBank home page.
    2. Click on the "Forgot login info?" link.
- **Expected Result:** The user is redirected to the "Customer Look-up" page.

