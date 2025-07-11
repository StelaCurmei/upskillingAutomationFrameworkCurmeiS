Feature: Login

  Background:
    Given log in page is accessed

  @Ui
  Scenario: Verify a user can log in with valid data
    When a new user is signed up
    And the signed up user enters valid login data
    And the user clicks on the submit button
    Then the user is redirected to contact list page

  @run
  @Ui
  Scenario: Verify a user cannot log in with invalid data
    When the user attempts to log in with invalid credentials:
      | email                       | password             |
      | om2345ag@ef345il.zdsm       | I*Z8QY7I!Yz63Vs      |
      | alomer.haag@gmail.comidUser | *Z8QYcdftYss         |
      | er.haag@g ail.User          | I*Z8QYcfvgh7I!Yz63Vs |
      |                             |                      |
      | er.haag@g ail.User          |                      |
      |                             | I*Z8QY7I!Yz63Vs      |
    Then the system displays an error message for each set of credentials
      | expected_error_message         |
      | Incorrect username or password |
      | Incorrect username or password |
      | Incorrect username or password |
      | Incorrect username or password |
      | Incorrect username or password |
      | Incorrect username or password |