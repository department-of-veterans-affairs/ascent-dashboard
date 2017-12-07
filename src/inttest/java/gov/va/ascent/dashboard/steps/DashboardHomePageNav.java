package gov.va.ascent.dashboard.steps;

import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import gov.va.ascent.dashboard.pages.DashboardHomePage;
import gov.va.ascent.dashboard.pages.DashboardLoginPage;
import gov.va.ascent.test.framework.restassured.BaseStepDef;
import gov.va.ascent.test.framework.selenium.BasePage;

public class DashboardHomePageNav extends BaseStepDef {

	DashboardHomePage homePage = null;
	
	final Logger log = LoggerFactory.getLogger(DashboardHomePageNav.class);

	@Before({ "@dashboardhomepagenav" })
	public void setUpREST() {
		//initREST();
	}

	@Given("^A user logins to dashboard homepage$")
	public void passTheHeaderInformationForDashboard() throws Throwable {
		//passHeaderInformation(tblHeader);
		//URL url = DashboardHomePageNav.class.getClassLoader().getResource("IEDriverServer.exe");
		//System.out.println("webdriver.chrome.driver=============="+ url.toString());
		WebDriver selenium = BasePage.getDriver();
		DashboardLoginPage loginPage = new DashboardLoginPage(selenium);
		loginPage.enterCredentialsandLogin();
		homePage = new DashboardHomePage(selenium);
		
	}

	@When("^the user is in dashboard page$")
	public void makerequesustoappsurlGet() throws Throwable {
		//invokeAPIUsingGet("", "dashboardURL");
	}
	@Then("^verify the Home button in the navigation menu$")
	public void verifyHomeButton () throws Throwable {
		
		homePage.getHomeButtonValue();
		Assert.assertEquals(homePage.getHomeButtonValue(), "Home");
	}

	@And("^verify the other links in the navigation menu$")
	public void otherLinks () throws Throwable {
		
		homePage.getSpringBootLinkElementValue();
		Assert.assertEquals(homePage.getSpringBootLinkElementValue(), "Spring Boot Admin (SBA)");
		homePage.getSwaggerLinkElementValue();
		Assert.assertEquals(homePage.getSwaggerLinkElementValue(), "Swagger");
		homePage.getZipkinLinkElementValue();
		Assert.assertEquals(homePage.getZipkinLinkElementValue(), "Zipkin");
		homePage.getKibanaLinkElementValue();
		Assert.assertEquals(homePage.getKibanaLinkElementValue(), "Kibana");
		}
	@After({ "@dashboardhomepagenav" })
	public void cleanUp(Scenario scenario) {
		postProcess(scenario);
		BasePage.closeBrowser();
	}

}
