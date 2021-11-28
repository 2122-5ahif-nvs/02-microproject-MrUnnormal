Feature: Update Parkingspot in database

  Background:
    * url baseUrl

  Scenario: Create location with put
    Given path 'api/parkingspot/'
    And request read ('parkingspot.json')
    When method PUT
    Then status 201
    Then match responseHeaders['Location'][0] == "#regex http://localhost:8081/api/parkingspot/.*"

  Scenario: Modify location with put
    Given path 'api/parkingspot/'
    And request {"location":{"zipcode":"4060","name":"Garagen City","id":41},"id":27,"position":4,"type":"Default","pricePerDay":25.0}
    When method PUT
    Then status 200