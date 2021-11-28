Feature: Add Location to database

  Background:
    * url baseUrl

  Scenario: Post location
    Given path 'api/location'
    And request read ('location.json')
    When method POST
    Then status 201
    Then match responseHeaders['Location'][0] == "#regex http://localhost:8081/api/location/.*"

