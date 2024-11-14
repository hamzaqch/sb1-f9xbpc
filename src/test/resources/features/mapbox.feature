Feature: Mapbox Map Functionality
  As a user
  I want to interact with Mapbox maps
  So that I can verify map functionality

  Background:
    Given I am on the Mapbox map page

  Scenario: Verify map loads successfully
    Then the map should be visible
    And the zoom controls should be present

  Scenario: Verify map zoom functionality
    When I click the zoom in button
    Then the map zoom level should increase
    When I click the zoom out button
    Then the map zoom level should decrease

  Scenario: Verify map pan functionality
    When I pan the map to the right
    Then the map center coordinates should change
    When I pan the map to the left
    Then the map center coordinates should change

  Scenario: Verify map style switching
    When I switch to "satellite-v9" style
    Then the map style should change to "satellite-v9"
    When I switch to "streets-v11" style
    Then the map style should change to "streets-v11"