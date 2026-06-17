Feature: Customer Lookup — Forgot Login Info Validation
  As a ParaBank user
  I want to test the "Forgot Login Info" form validation
  So that I can verify all error messages appear at the right time

  Scenario: Customer Lookup form validation flow
    Given I am on the ParaBank home page
    When I click the "Forgot Login Info" link
    Then the Customer Lookup panel should be visible
    And all fields should be empty by default
    And the "Find My Login Info" button should be available

    When I click the "Find My Login Info" button with all fields empty
    Then appropriate error messages should appear
    And the amount of error messages should match the number of required fields

    When I fill all fields except SSN and click "Find My Login Info"
    Then only one message "SSN is required" should be visible
    And the previous error messages should have disappeared

    When I click the "Find My Login Info" button again
    Then the SSN is required message should still be visible

    When I enter any SSN number and click "Find My Login Info"
    Then the error "The customer information provided could not be found" should be displayed

    When I click on the ParaBank logo
    Then the home page should be visible

    When I mouse over the ParaBank logo
    Then a tooltip with text "ParaBank" should be visible
