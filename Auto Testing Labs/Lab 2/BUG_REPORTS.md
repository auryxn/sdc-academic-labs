# Lab 2: Bug Reports and Enhancements for ParaBank

**Task:** Find two defects or enhancements for ParaBank and write professional reports.
**URL:** https://parabank.parasoft.com/parabank/index.htm
**Deadline:** 2026.04.12

---

## Bug Report #1: Missing Input Validation for Zip Code Field

**ID:** PB-BUG-001  
**Severity:** Medium  
**Priority:** Medium  
**Status:** Open  

**Description:**  
The "Zip Code" field on the Registration page does not validate numeric input. Users can enter alphabetic characters or symbols, which are accepted by the system upon form submission.

**Steps to Reproduce:**  
1. Navigate to `https://parabank.parasoft.com/parabank/register.htm`.
2. Fill in all required registration fields with valid data.
3. In the "Zip Code" field, enter non-numeric characters (e.g., "ABCDE").
4. Click the "Register" button.

**Expected Result:**  
The system should display an error message (e.g., "Zip code must be numeric") or restrict the field to numeric input only.

**Actual Result:**  
The system accepts the alphabetic input and proceeds with the registration process (or fails with a generic server error depending on backend state), allowing inconsistent data in the database.

---

## Enhancement Report #2: Implement Password Strength Indicator

**ID:** PB-ENH-002  
**Severity:** Low (Feature Request)  
**Priority:** Low  
**Status:** Proposed  

**Description:**  
Currently, the registration form accepts passwords of any length and complexity (including single-character passwords). Adding a real-time password strength indicator would improve security by encouraging users to create more complex passwords.

**Proposed Changes:**  
1. Add a visual indicator (e.g., a progress bar or text label: Weak, Medium, Strong) below the "Password" field on the Registration page.
2. The indicator should update dynamically as the user types, based on criteria like length, special characters, and mixed case.

**Benefit:**  
Enhances overall system security and provides a better user experience (UX) by guiding the user toward secure practices during account creation.

**Steps to Verify:**  
1. Navigate to the Register page.
2. Type "123" in the password field; observe "Weak" indicator.
3. Type "ComplexPass!2026" in the password field; observe "Strong" indicator.
