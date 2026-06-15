# Task #7 — Framework

**Is Required:** Implement test/tests to cover the defect/enchantment found and reported within the scope of Task #2.

**Target website:** https://parabank.parasoft.com/parabank/index.htm

**Notes:**
- You can implement your tests either as factory tests or as BDD scenario according to your wishes
- Your defect should be unique. Please check this with your classmates
- The final score will be set after the test demonstration

## Implemented Bug
**PB-BUG-001:** Missing Input Validation for Zip Code Field
- Implemented as BDD scenario (`zipcode_validation.feature`)
- Test fills registration form with non-numeric zip code ("ABCDE")
- Asserts that validation error should appear → test will FAIL, proving the bug
