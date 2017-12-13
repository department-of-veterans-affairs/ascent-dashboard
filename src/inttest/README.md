# README #

This document provides the details of **Ascent Dashboard Acceptance test** .

## Acceptance test for ascent Dashboard ##

Acceptance test are created to make sure the core services in ascent dashboard are working as expected.
This project uses Java - Maven platform, the REST-Assured jars for core API validations. It also uses selenium webdriver for UI validation.


## Project Structure ##

gov/va/ascent/features - This is where you will create the cucumber feature files that contain the Feature and Scenarios for the dashboard service you are testing.

log4j - File used to store all the log4j properties.

src/inttest/java/gov /va/ascent/dashboard /steps- The implementation steps related to the feature and scenarios mentioned in the cucumber file for the API needs to be created in this location.

src/inttest/java/gov /va/ascent/dashboard /pages-  The object repository for each page is specified here. For each components or page there should be a corresponding page class. This Page class will find the Web Elements of that web page and contains Page methods which perform operations on those Web Elements.

src/inttest/java/gov/va/ascent/dashboard/runner - Cucumber runner class that contains all feature file entries that needs to be executed at runtime. The annotations provided in the cucumber runner class will assist in bridging the features to step definitions.

vetapi.properties – configuration properties such as URL are specified here.

## Execution ##
**Command Line:** Use this command to execute the dashboard acceptance test. 
mvn verify –Pinttest

Note: By default, mvn verify –Pinttest executes the test in headless browser

mvn clean verify -Pinttest -Dbrowser=BrowserName

Here BrowserName  can be “HtmlUnit” or “CHROME”

If you want to execute the test in chrome browser. Use this below command. 

mvn clean verify -Pinttest -Dbrowser=CHROME -DwebdriverPath=”Path of the chrome driver”

Use this command to execute for different environment.

mvn verify -Pinttest -Dbrowser=HtmlUnit -Dtest.env=<env> -DX-Vault-Token=<token> 

Note: env is the environment 
The parameter X-Vault-Token is not valid for local environment. It is passed thru pipeline. 
