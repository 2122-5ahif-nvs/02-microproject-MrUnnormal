Feature: Add Parkingspot to database

  Background:
    * url baseUrl

  Scenario: Post location
    Given path 'api/parkingspot'
    And request read ('parkingspot.json')
    When method POST
    Then status 201
    Then match responseHeaders['Location'][0] == "#regex http://localhost:8081/api/parkingspot/.*"

