Feature: Price inquiry

  As a client of the price REST API
  I want to retrieve the applicable price for a product
  So that the correct tariff is returned for the requested date and time

  Scenario: Test 1 - Request at 10:00 on June 14 for product 35455 and brand 1
    Given the product "35455" and brand "1" exist
    When I request the price at "2020-06-14T10:00:00Z"
    Then the response status should be 200
    And the returned product should be "35455"
    And the returned brand should be "1"
    And the returned price list should be "1"
    And the returned price should be 35.50

  Scenario: Test 2 - Request at 16:00 on June 14 for product 35455 and brand 1
    Given the product "35455" and brand "1" exist
    When I request the price at "2020-06-14T16:00:00Z"
    Then the response status should be 200
    And the returned product should be "35455"
    And the returned brand should be "1"
    And the returned price list should be "2"
    And the returned price should be 25.45

  Scenario: Test 3 - Request at 21:00 on June 14 for product 35455 and brand 1
    Given the product "35455" and brand "1" exist
    When I request the price at "2020-06-14T21:00:00Z"
    Then the response status should be 200
    And the returned product should be "35455"
    And the returned brand should be "1"
    And the returned price list should be "1"
    And the returned price should be 35.50

  Scenario: Test 4 - Request at 10:00 on June 15 for product 35455 and brand 1
    Given the product "35455" and brand "1" exist
    When I request the price at "2020-06-15T10:00:00Z"
    Then the response status should be 200
    And the returned product should be "35455"
    And the returned brand should be "1"
    And the returned price list should be "3"
    And the returned price should be 30.50

  Scenario: Test 5 - Request at 21:00 on June 16 for product 35455 and brand 1
    Given the product "35455" and brand "1" exist
    When I request the price at "2020-06-16T21:00:00Z"
    Then the response status should be 200
    And the returned product should be "35455"
    And the returned brand should be "1"
    And the returned price list should be "4"
    And the returned price should be 38.95

  Scenario: Requesting a price for an unknown product
    Given the product "99999" does not exist
    When I request the price
    Then the response status should be 404
    And the response should contain the error message "Product not found"
