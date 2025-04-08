Feature: Login

  @OpenBrowser
  @run
  Scenario: Verify a user can log in with valid data
    Given A new user signs up
#    And The login page is accessed
    When The user enters valid login data
    And The user clicks on the submit button
    Then The user is redirected to contact list page