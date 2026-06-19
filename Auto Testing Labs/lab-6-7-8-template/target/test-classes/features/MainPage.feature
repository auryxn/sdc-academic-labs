Feature: Main Page Functionality

  Scenario: Verify Navigation to Registration
    Given User is on the Main page
    When User clicks on the Register link
    Then User is redirected to the Registration page

  Scenario: Verify ParaBank Logo on Main Page
    Given User is on the Main page
    Then ParaBank logo should be displayed

