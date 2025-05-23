Feature: Product Management
  As a user of the system
  I want to manage products
  So that I can keep track of my inventory

  Scenario: Create a new product
    Given I have a product with the following details:
      | name        | description           | price | stockQuantity |
      | Test Product| A test product        | 10.99 | 100           |
    When I create the product
    Then the product should be created successfully
    And the product should have an ID
    And the product details should match the input

  Scenario: Get a product by ID
    Given I have a product in the system
    When I request the product by its ID
    Then I should receive the product details

  Scenario: Get all products
    Given I have multiple products in the system
    When I request all products
    Then I should receive a list of all products

  Scenario: Update a product
    Given I have a product in the system
    When I update the product with the following details:
      | name              | description                 | price | stockQuantity |
      | Updated Product   | An updated test product     | 19.99 | 50            |
    Then the product should be updated successfully
    And the product details should match the updated values

  Scenario: Delete a product
    Given I have a product in the system
    When I delete the product
    Then the product should be deleted successfully
    And the product should no longer be available