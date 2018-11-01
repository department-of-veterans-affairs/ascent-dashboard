package gov.va.ascent.dashboard.util;

import gov.va.ascent.test.framework.service.RESTConfigService;

public class DashboardAppUtil {

	private DashboardAppUtil() {
		
	}
	
	public static String getBaseURL() {
		return RESTConfigService.getBaseURL("data.'username'", "data.'password'");
		
	}
	public static String getBaseURLWithNoAuth() {
		return RESTConfigService.getInstance().getProperty("baseURL", true);		
	}
	
	
}
