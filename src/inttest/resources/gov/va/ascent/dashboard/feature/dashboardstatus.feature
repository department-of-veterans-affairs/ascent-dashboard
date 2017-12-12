Feature: Log in to dashboard service to check the service is up 

@dashboardstatus
  Scenario: Log in to dashboard service to check the status 
      Given I pass the header information for dashboard service
      | Pragma       | no-cache        |
      When user makes a request to dashboard URL
      Then the response code must be for dashboard service 200
    