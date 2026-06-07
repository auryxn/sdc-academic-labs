# Task #9 — API testing task

**Given:** https://jsonplaceholder.typicode.com/

Select any two requests from the list under the Routes section.

**Required:**
1. Create a separate git repository. Create an own maven project with unit test library (e.g. TestNG or JUnit).
2. Create a test suite for testing the following methods from this REST service using Java:
   - Tests should be built with layered architecture (core, domain, tests levels)
   - Tests should be created using either Rest Assured or Spring Rest Template or Apache Http Client
3. Tests have to include critical path tests validations, both positive and negative (define a set of tests on your own).
   - Besides status validation, check something else from the response.

## Implementation
- **Library:** Rest Assured 5.4.0 + TestNG 7.10.2
- **Endpoints:** `/posts` and `/comments`
- **Architecture:** 3-layer (core, domain, tests)
- **Tests:** 16 tests total (8 positive, 4 negative, 4 smoke)
