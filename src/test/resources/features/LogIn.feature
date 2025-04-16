Feature: Login

  @Ui
#  @run
  Scenario: Verify a user can log in with valid data
    Given log in page is accessed
    And a new user is signed up
    When the signed up user enters valid login data
    And the user clicks on the submit button
    Then the user is redirected to contact list page


  @run
  Scenario: Verify a user cannot log in with invalid data using a data table
    Given log in page is accessed
    And a new user is signed up
    When the user attempts to log in with invalid credentials:
      | username    | password    | expected_error_message              |
      | invalidUser | validPass   | "Invalid username."                 |
      | validUser   | wrongPass   | "Invalid password."                 |
      | invalidUser | wrongPass   | "Invalid username or password."     |
    Then the system should display the corresponding error message for each set of credentials