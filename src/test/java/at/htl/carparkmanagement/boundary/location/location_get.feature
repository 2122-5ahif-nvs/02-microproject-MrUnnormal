Feature: Get Location from database

  Background:
    * url baseUrl

  Scenario: Get all locations
    Given path 'api/location'
    And request read ('location.json')
    When method GET
    Then status 200
    Then match response == [{"name": "Garagen City","zipcode":"4060", id:5 }]

  Scenario: Get location with id 1
    Given path 'api/location/5'
    When method GET
    Then status 200
    Then match response == { "name": "Garagen City","zipcode":"4060", id:5 }

  Scenario: Get nonexistent location
    Given path 'api/location/999'
    When method GET
    Then status 204
