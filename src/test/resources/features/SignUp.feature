Feature: SignUp

  Background:
    Given log in page is accessed

   # @run
  @Ui
  Scenario: Verify a new user is signed up with valid data
    When the user clicks the Sign Up button
    And the user enters valid sign up data
    And the user clicks the Submit button
    Then the user is redirected to the Contact List page

  @Ui
  Scenario: Verify a new user cannot sign in with invalid data
    When the user attempts to sign in with invalid data:
      | email                       | password             |
      | om2345ag@ef345il.zdsm       | I*Z8QY7I!Yz63Vs      |
      | alomer.haag@gmail.comidUser | *Z8QYcdftYss         |
      | er.haag@g ail.User          | I*Z8QYcfvgh7I!Yz63Vs |
    Then the system displays an error message for each set of credentials
      | expected_error_message         |
      | Incorrect username or password |
      | Incorrect username or password |
      | Incorrect username or password |