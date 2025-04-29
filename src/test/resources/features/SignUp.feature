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
