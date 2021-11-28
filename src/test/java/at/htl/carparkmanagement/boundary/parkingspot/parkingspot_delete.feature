Feature: Delete Parkingspot from database

  Background:
    * url baseUrl

  Scenario: Delete with content
    Given path 'api/parkingspot'
    And request read ('parkingspot.json')
    When method POST
    Then status 201
    Then match responseHeaders['Location'][0] == "#regex http://localhost:8081/api/parkingspot/.*"

    Given path 'api/parkingspot/26'
    When method DELETE
    Then status 200
    Then match response == "delete successful"

  Scenario: Delete with no content
    Given path 'api/location/999'
    When method GET
    Then status 204
