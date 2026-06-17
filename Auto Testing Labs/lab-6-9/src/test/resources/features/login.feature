Feature: Customer Login
  As a registered user
  I want to log into my account
  So that I can manage my finances

  Scenario: Successful login with valid credentials
    Given I am on the ParaBank home page
    When I enter username "john" and password "demo"
    And I click the Login button
    Then I should see the Account Overview page with a welcome message