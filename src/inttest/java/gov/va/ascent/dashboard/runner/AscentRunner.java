package gov.va.ascent.dashboard.runner;

import org.junit.runner.RunWith;
import org.testng.annotations.BeforeSuite;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import cucumber.api.testng.AbstractTestNGCucumberTests;

@RunWith(Cucumber.class)
@CucumberOptions(strict = false, format = { "pretty",

		"html:target/site/cucumber-pretty", "json:target/cucumber.json" }, features = {

				"src/inttest/resources/gov/va/ascent/feature/dashboardhomepagenav.feature",
				"src/inttest/resources/gov/va/ascent/feature/dashboardstatus.feature" },

		glue = { "gov.va.ascent.dashboard.steps" })
public class AscentRunner extends AbstractTestNGCucumberTests {

	@BeforeSuite(alwaysRun = true)
	public void setUp() throws Exception {
		System.out.println("Before run------------------------");
	}

}
