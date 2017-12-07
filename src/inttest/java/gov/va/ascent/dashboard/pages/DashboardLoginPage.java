package gov.va.ascent.dashboard.pages;

import org.openqa.selenium.WebDriver;

import gov.va.ascent.test.framework.selenium.BasePage;
import gov.va.ascent.test.framework.service.RESTConfigService;
import gov.va.ascent.test.framework.service.VaultService;

public class DashboardLoginPage extends BasePage{
	
    public DashboardLoginPage(WebDriver selenium) {
        super(selenium);
        initialize(this);
    }
    public void enterCredentialsandLogin() throws InterruptedException {
        try {
        		BasePage.getDriver().get(getDashboardUrl());
        		
        }
        catch (Exception e) {
            e.printStackTrace();
            
        }
    }
    
    private String getDashboardUrl()  {
    		RESTConfigService restConfig =  RESTConfigService.getInstance();
    		String dashboardURL =  restConfig.getPropertyName("dashboardURL");
    		return VaultService.replaceUrlWithVaultCredentialDashboard(dashboardURL);
    }

}