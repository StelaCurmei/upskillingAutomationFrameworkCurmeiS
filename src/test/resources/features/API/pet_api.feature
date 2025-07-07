@run
Feature: PetStore CRUD operations

  Background:
    Given I set the API base URL to the PetStore service

  Scenario: Create a new Pet
    When I POST a new Pet with name "Fido" and status "available"
    Then the response status code should be 200
    And the response body should contain a pet object with name "Fido" and status "available"

  Scenario: Get an existing Pet
    Given a pet has been created with name "Spot" and status "available"
    When I GET that pet by its id
    Then the response status code should be 200
    And the response body should contain a pet object with id matching the created id

  Scenario: Update an existing Pet
    Given there is an existing pet with id 1234
    When I PUT to update that pet’s status to "sold"
    Then the response status code should be 200
    And the response body should show status "sold"

  Scenario: Delete a Pet
    Given there is an existing pet with id 1234
    When I DELETE that pet by its id
    Then the response status code should be 200
    And a subsequent GET by that id returns 404
