Feature: Zip Code Validation Bug
  As a user registering on ParaBank
  I want the Zip Code field to accept only numeric input
  So that data consistency is maintained in the system

  @BUG
  Scenario: Zip Code field accepts non-numeric characters
    Given I am on the ParaBank registration page
    When I enter non-numeric characters "ABCDE" in the Zip Code field
    And I submit the registration form with valid data
    Then the system should reject non-numeric zip codes and display a validation error