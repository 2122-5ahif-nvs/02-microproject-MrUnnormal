Feature: Update Location in database

  Background:
    * url baseUrl

  Scenario: Create location with put
    Given path 'api/location/'
    And request read ('location.json')
    When method PUT
    Then status 201
    Then match responseHeaders['Location'][0] == "#regex http://localhost:8081/api/location/.*"

  Scenario: Modify location with put
    Given path 'api/location/'
    And request {id:6,"name": "Garagen-City","zipcode": 4060}
    When method PUT
    Then status 200