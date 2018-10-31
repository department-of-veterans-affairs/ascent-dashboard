package gov.va.ascent.dashboard.pages;

import org.openqa.selenium.WebDriver;

import gov.va.ascent.dashboard.util.DashboardAppUtil;
import gov.va.ascent.test.framework.selenium.BasePage;

public class DashboardLoginPage extends BasePage{
	
    public DashboardLoginPage(WebDriver selenium) {
        super(selenium);
        initialize(this);
    }
    public void enterCredentialsandLogin() throws InterruptedException {
        try {
        	BasePage.getDriver().get(DashboardAppUtil.getBaseURLWithNoAuth());
        		
        }
        catch (Exception e) {
            e.printStackTrace();
            
        }
    }

}

