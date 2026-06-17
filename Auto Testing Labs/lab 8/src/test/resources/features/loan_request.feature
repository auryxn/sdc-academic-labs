Feature: Loan Request
  As a registered ParaBank customer
  I want to apply for different loan amounts
  So that I can verify the loan approval system behavior

  Scenario Outline: Apply for a loan with specific amount and down payment
    Given I am logged into ParaBank with valid credentials
    When I navigate to the Request Loan page
    And I enter loan amount "<amount>"
    And I enter down payment "<down_payment>"
    And I click the Apply Now button
    Then I should see a loan response with status "<status>"

    Examples:
      | amount  | down_payment | status   |
      | 1000    | 100          | Approved |
      | 5000    | 500          | Approved |
      | 25000   | 2500         | Approved |
      | 100000  | 10000        | Denied   |
