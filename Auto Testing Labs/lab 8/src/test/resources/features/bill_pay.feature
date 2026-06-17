Feature: Bill Pay
  As a registered ParaBank customer
  I want to pay a bill to a payee
  So that I can transfer funds to another account

  Scenario: Successful bill payment with valid payee details
    Given I am logged into ParaBank with valid credentials
    When I navigate to the Bill Pay page
    And I enter payee name "John Doe"
    And I enter payee address "123 Main St, New York, NY 10001"
    And I enter payee phone number "+1234567890"
    And I enter payee account number "12345"
    And I enter payment amount "100.00"
    And I click the Send Payment button
    Then I should see a bill payment confirmation message
