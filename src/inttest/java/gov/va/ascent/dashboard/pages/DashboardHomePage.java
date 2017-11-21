package gov.va.ascent.dashboard.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gov.va.ascent.selenium.common.BasePage;
import gov.va.ascent.util.RESTConfig;




	public class DashboardHomePage extends BasePage{
		
		final Logger log = LoggerFactory.getLogger(RESTConfig.class);

		 public DashboardHomePage(WebDriver selenium) {
			 super(selenium);
			 initialize(this);
			 
		 }

		 //Home Button in Dashboard Navigation
		 @FindBy (xpath="//div[@class='header clearfix']/nav/ul/li[1]/a")
		 WebElement homebutton;
		 //Spring Boot tab in navigation menu
		 @FindBy (xpath="//div[@class='header clearfix']/nav/ul/li[2]/a")
		 WebElement springbootlink;
		 
		//swagger tab in navigation menu
		 @FindBy (xpath="//div[@class='header clearfix']/nav/ul/li[3]/a")
		 WebElement swaggerlink;
		
		 //zippkin tab in navigation menu
		 @FindBy (xpath="//div[@class='header clearfix']/nav/ul/li[4]/a")
		 WebElement zipkinlink;
		 
		 //Kibana tab in navigation menu
		 @FindBy (xpath="//div[@class='header clearfix']/nav/ul/li[5]/a")
		 WebElement kibanalink;
		 
		 public String getHomeButtonValue() {
			 return homebutton.getText();
		 }
	 
		 public String getSpringBootLinkElementValue() {
			 return springbootlink.getText();
		 }
		 public String getSwaggerLinkElementValue() {
			 return swaggerlink.getText();
		 }

		 public String getZipkinLinkElementValue() {
			 return zipkinlink.getText();
		 }
		 public String getKibanaLinkElementValue() {
			 return kibanalink.getText();
		 }
		 
	}
