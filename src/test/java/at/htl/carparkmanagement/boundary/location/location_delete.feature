Feature: Delete Location from database

  Background:
    * url baseUrl

  Scenario: Delete with content
    Given path 'api/location/5'
    When method DELETE
    Then status 200
    Then match response == "delete successful"

  Scenario: Delete with no content
    Given path 'api/location/999'
    When method GET
    Then status 204
