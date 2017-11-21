Feature: Verify the navigation bar in the dashboard home page 
@dashboardhomepagenav
  Scenario: Verify the navigation bar links in the dashboard home page
      Given A user logins to dashboard homepage
      When the user is in dashboard page
      Then verify the Home button in the navigation menu
      And verify the other links in the navigation menu
      
      