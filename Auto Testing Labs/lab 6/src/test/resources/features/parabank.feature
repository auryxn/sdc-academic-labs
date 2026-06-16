Feature: ParaBank Smoke Test Scenarios

  Background:
    Given I am on the ParaBank main page

  @Smoke
  Scenario: Verify main page logo and register link navigation
    Then the ParaBank logo should be visible
    When I click the Register link
    Then "Signing up is easy!" registration title should be displayed

  @Smoke
  Scenario: Verify error message on invalid login
    When I enter username "invalidUser"
    And I enter password "wrongPass"
    And I click Log In
    Then an error message should be displayed
    And the error message should contain "error"
