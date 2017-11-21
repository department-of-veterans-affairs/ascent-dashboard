package gov.va.ascent.dashboard.pages;

import org.openqa.selenium.WebDriver;

import gov.va.ascent.selenium.common.BasePage;
import gov.va.ascent.selenium.utils.PropertiesLoader;

public class DashboardLoginPage extends BasePage{
    public DashboardLoginPage(WebDriver selenium) {
        super(selenium);
        initialize(this);
    }
    public void enterCredentialsandLogin() throws InterruptedException {
        try {
        	PropertiesLoader PropertiesLoader = new PropertiesLoader();
        	String ascent_url = (String) PropertiesLoader.loadBaseProperties().get("ascent.server.url");
        	BasePage.getDriver().get(ascent_url);
        //Log.Logger("INFO", "Logging into DiscoveryLogin Page ..", null);
        }
        catch (Exception e) {
            e.printStackTrace();
            //Log.Logger("ERROR","Could not login to DiscoveryLoginPage using Credentials",e);
        }
    }

}