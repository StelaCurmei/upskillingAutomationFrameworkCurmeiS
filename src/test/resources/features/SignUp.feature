Feature: SignUp

  Background:
    Given log in page is accessed
    And the user clicks the Sign Up button

  @Ui
  Scenario: Verify a new user is signed up with valid data
    When the user enters valid sign up data
    And the user clicks the Submit button
    Then the user is redirected to the Contact List page


  @run
  @Ui
  Scenario: Verify a new user cannot sign up with invalid data
    When the user attempts to sign in with invalid data:
      | firstname | lastname | email         | password                                                                                              |
      |           |          |               |                                                                                                       |
      |           |          |               | Test123#                                                                                              |
      |           |          | abc@test.test | Test123#                                                                                              |
      | Abc       |          | abc@test.test | Test123#                                                                                              |
      |           | Def      | abc@test.test | Test123#                                                                                              |
      | Abc       | Def      | abc@test.test |                                                                                                       |
      | Abc       | Def      |               | Test123#                                                                                              |
      | Abc       | Def      | abc@          | Test123#                                                                                              |
      | Abc       | Def      | abc@test      | Test123#                                                                                              |
      | Abc       | Def      | abc@test.test | 111111                                                                                                |
      | Abc       | Def      | abc@test.test | 11111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111 |
    Then the system displays an error message for each set of invalid data
      | expected_error_message                                                                                                                                                                                                                |
      | User validation failed: firstName: Path `firstName` is required., lastName: Path `lastName` is required., email: Email is invalid, password: Path `password` is required.                                                             |
      | User validation failed: firstName: Path `firstName` is required., lastName: Path `lastName` is required., email: Email is invalid                                                                                                     |
      | User validation failed: firstName: Path `firstName` is required., lastName: Path `lastName` is required.                                                                                                                              |
      | User validation failed: lastName: Path `lastName` is required.                                                                                                                                                                        |
      | User validation failed: firstName: Path `firstName` is required.                                                                                                                                                                      |
      | User validation failed: password: Path `password` is required.                                                                                                                                                                        |
      | User validation failed: email: Email is invalid                                                                                                                                                                                       |
      | User validation failed: email: Email is invalid                                                                                                                                                                                       |
      | User validation failed: email: Email is invalid                                                                                                                                                                                       |
      | User validation failed: password: Path `password` (`111111`) is shorter than the minimum allowed length (7).                                                                                                 |
      | User validation failed: password: Path `password` (`11111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111`) is longer than the maximum allowed length (100). |
