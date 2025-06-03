@run
@api
Feature: PetStore API – Add a new Pet

  Scenario: Add a new pet to the store
    Given I prepare a pet payload with:
      | id    | name   | category.id | category.name | photoUrls                       | tag.id | tag.name | status      |
      | 12345 | "Fido" | 1           | "dogs"        | ["http://example.com/fido.jpg"] | 99     | "friend" | "available" |
    When I send a POST request to "/pet"
    Then the response status code should be 200
    And the response JSON should contain:
      | $.id            | 12345       |
      | $.name          | "Fido"      |
      | $.category.name | "dogs"      |
      | $.status        | "available" |