package gov.va.ascent.dashboard.pages;

import org.openqa.selenium.WebDriver;

import gov.va.ascent.dashboard.util.AppUtil;
import gov.va.ascent.test.framework.selenium.BasePage;

public class DashboardLoginPage extends BasePage{
	
    public DashboardLoginPage(WebDriver selenium) {
        super(selenium);
        initialize(this);
    }
    public void enterCredentialsandLogin() throws InterruptedException {
        try {
        		BasePage.getDriver().get(AppUtil.getBaseURL());
        		
        }
        catch (Exception e) {
            e.printStackTrace();
            
        }
    }

}