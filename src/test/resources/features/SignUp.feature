Feature: SignUp

  @Ui
#  @run
  Scenario: Verify a new user is signed up with valid data
    Given Log in page is accessed
    And Sign Up button is clicked
    When Valid sign in data is entered
    And Submit button is clicked
    Then User is redirected to Contact list page