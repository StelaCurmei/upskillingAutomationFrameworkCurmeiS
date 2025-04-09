Feature: Login

  @Ui
  @run
  Scenario: Verify a user can log in with valid data
    Given log in page is accessed
    And a new user is signed up
    When the signed up user enters valid login data
    And the user clicks on the submit button
    Then the user is redirected to contact list page