Feature: Task 8 Individual Assignment Functionalities

  Background:
    Given The user is registered and logged in

  Scenario: User navigation and validation scenario
    When User navigates to the Account Overview page
    Then The Account Overview page title should be verified
    When User clicks Log Out
    Then User is successfully logged out and redirected to the login page
    When User attempts to log in with empty credentials
    Then A validation error message should be displayed with correct text and color
    When User attempts to log in with a username but empty password
    Then The entered credentials should be cleared
    And A validation error message should be displayed with correct text and color
    When User navigates to the About page
    Then The About page title should be verified
    When User clicks the link to parasoft.com on the About page
    And User clicks the browser Back button
    Then The current page should be identical to the About page

  Scenario Outline: Contact Us form validation
    Given User is on the Contact Us page
    When User submits the Contact Us form with name "<name>", email "<email>", phone "<phone>", message "<message>"
    Then The Contact Us form should display validation errors or success message depending on inputs

    Examples:
      | name | email | phone | message |
      | John |       | 123   | Hello   |
      |      | a@b.c |       |         |
      | Test | test@test.com | 111 | Successful message |

