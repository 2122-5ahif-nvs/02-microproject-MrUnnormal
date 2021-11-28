Feature: Get Location from database

  Background:
    * url baseUrl

  Scenario: Get all parkingspots
    Given path 'api/parkingspot'
    When method GET
    Then status 200
    Then match response == [{"location":"Location{id=41, name='Garagen City', zipcode='4060'}","id":"25","type":"Default"}]

  Scenario: Get parkingspot with id _
    Given path 'api/parkingspot/25'
    When method GET
    Then status 200
    Then match response == {"location":{"zipcode":"4060","name":"Garagen City","id":41},"id":25,"position":0,"type":"Default","pricePerDay":0.0}

  Scenario: Get nonexistent parkingspot
    Given path 'api/parkingspot/999'
    When method GET
    Then status 204

  Scenario: Get free parkingspots
    Given path 'api/parkingspot/free/41'
    When method GET
    Then match response == [{"id":25,"location":{"id":41,"name":"Garagen City","zipcode":"4060"},"position":0,"pricePerDay":0.0,"type":"Default"}]

  Scenario: Get free parkingspots but location is not in db
    Given path 'api/parkingspot/free/999'
    When method GET
    Then match responseHeaders.ETag == ['"location not found"']

  Scenario: Get free parkingspots but no parkingspots
    Given path 'api/parkingspot/25'
    When method DELETE
    Then status 200
    Then match response == "delete successful"

    Given path 'api/parkingspot/free/41'
    When method GET
    Then match responseHeaders.ETag == ['"no free parkingspots"']