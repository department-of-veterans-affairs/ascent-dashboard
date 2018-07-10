package gov.va.ascent.dashboard.steps;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import gov.va.ascent.dashboard.util.DashboardAppUtil;
import gov.va.ascent.test.framework.restassured.BaseStepDef;


public class DashboardStatus extends BaseStepDef {

	final Logger log = LoggerFactory.getLogger(DashboardStatus.class);

	@Before({ "@dashboardstatus" })
	public void setUpREST() {
		initREST();
	}

	@Given("^I pass the header information for dashboard service$")
	public void passTheHeaderInformationForDashboard(Map<String, String> tblHeader) throws Throwable {
		passHeaderInformation(tblHeader);
	}

	@When("^user makes a request to dashboard URL$")
	public void makerequesustoappsurlGet() throws Throwable {
		invokeAPIUsingGet(DashboardAppUtil.getBaseURL(), false);
	}
	@Then("^the response code must be for dashboard service (\\d+)$")
	public void serviceresposestatuscodemustbe(int intStatusCode) throws Throwable {
		validateStatusCode(intStatusCode);
	}


	@After({ "@dashboardstatus" })
	public void cleanUp(Scenario scenario) {
		postProcess(scenario);
	}

}
