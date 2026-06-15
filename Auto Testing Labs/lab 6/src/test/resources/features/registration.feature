Feature: Registration
  As a new visitor
  I want to navigate to the registration page
  So that I can create an account

  Scenario: Navigate to registration page from home
    Given I am on the ParaBank home page
    When I click the Register link
    Then I should see the registration page with title "Signing up is easy!"