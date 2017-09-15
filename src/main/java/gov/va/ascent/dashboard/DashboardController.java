package gov.va.ascent.dashboard;

import java.io.IOException;

import java.util.Map;
import java.util.TreeMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * An <tt>Ascent Dashboard Controller</tt> to generate and serve the misc. dashboard URL requests
 *
 */
@Controller
public class DashboardController {

	private static final Logger LOGGER = LoggerFactory.getLogger(DashboardController.class);

	private static final String REST_API = "REST-API";
	private static final String APP_TYPE = "appType";
	private static final String API = "api/";

	@Autowired
    private DiscoveryClient discoveryClient;
	
	@RequestMapping("/")
    public String index() {
        return "index";
    }
	
    @RequestMapping("/hystrix-dash")
    public void hystrix(HttpServletRequest request, HttpServletResponse response) throws IOException {

    	ServiceInstance zuulInstance = getZuul();
    	
    	if(zuulInstance != null){
        	response.sendRedirect(ServletUriComponentsBuilder.fromCurrentContextPath().path("/hystrix/monitor")
        			.queryParam("stream", zuulInstance.getUri() + "/hystrix.stream")
        			.queryParam("delay", "5000")
        			.build().toUriString());
    	} else {
    		LOGGER.warn("zuul server not found in eureka discovery server, redirecting to hystrix dash for manual population!");
    		response.sendRedirect(ServletUriComponentsBuilder.fromCurrentContextPath().path("/hystrix").build().toUriString());
    	}
    }
    
    @RequestMapping("/turbine-dash")
    public void turbine(HttpServletRequest request, HttpServletResponse response) throws IOException {

    	ServiceInstance dashInstance = getDashboard();
    	
    	if(dashInstance != null){
        	response.sendRedirect(ServletUriComponentsBuilder.fromCurrentContextPath().path("/hystrix/monitor")
        			.queryParam("stream", dashInstance.getUri() + "/turbine.stream")
        			.build().toUriString());
    	} else {
    		LOGGER.warn("dashboard app not found in eureka discovery server, redirecting to hystrix dash for manual population!");
    		response.sendRedirect(ServletUriComponentsBuilder.fromCurrentContextPath().path("/hystrix").build().toUriString());
    	}
    }
    
    @RequestMapping("/swagger-dash")
    public String swagger(Model model) {
    	final ServiceInstance zuulInstance = getZuul();

    	final Map<String, String> swaggerApps = new TreeMap<>();

    	discoveryClient.getServices().forEach((String service) -> {

			final ServiceInstance firstServiceInstance = getFirstServiceInstance(service);

			if(firstServiceInstance != null
					&& REST_API.equals(firstServiceInstance.getMetadata().get(APP_TYPE))){
				swaggerApps.put(firstServiceInstance.getServiceId().toLowerCase(),
						ServletUriComponentsBuilder.fromUri(zuulInstance.getUri())
								.path(API)
								.path(firstServiceInstance.getServiceId().toLowerCase())
								.path("/swagger-ui.html")
								.build().toUriString());
			}
		});

    	model.addAttribute("swaggerApps", swaggerApps);
    	model.addAttribute("currentPageTitle", "Swagger URLs");
        return "swagger";
    }
    
    @RequestMapping("/monitor-dash")
    public String monitor(Model model) {
    	final ServiceInstance zuulInstance = getZuul();
    	
    	final Map<String, String> gatewayUrls = new TreeMap<>();

    	discoveryClient.getServices().forEach((String service) -> {

    		final ServiceInstance firstServiceInstance = getFirstServiceInstance(service);

			if(firstServiceInstance != null
					&& REST_API.equals(firstServiceInstance.getMetadata().get(APP_TYPE))) {
				gatewayUrls.put(service, ServletUriComponentsBuilder.fromUri(zuulInstance.getUri())
						.path(API)
						.path(firstServiceInstance.getServiceId().toLowerCase())
						.build().toUriString());
			}
		});
    	model.addAttribute("gatewayUrls", gatewayUrls);
		model.addAttribute("currentPageTitle", "Monitor URLs");
        return "monitor";
    }
    

	private ServiceInstance getZuul() {
		ServiceInstance zuulInstance = null;
    	
    	for(ServiceInstance serviceInstance: discoveryClient.getInstances("ascent-gateway")){
    		zuulInstance = serviceInstance;
    		break;
    	}
		return zuulInstance;
	}
	
	private ServiceInstance getDashboard() {
		ServiceInstance dashInstance = null;
		
    	for(ServiceInstance serviceInstance: discoveryClient.getInstances("ascent-dashboard")){
    		dashInstance = serviceInstance;
    		break;
    	}
		return dashInstance;
	}

	/**
	 * Returns the first instance of the provided service name using the DiscoveryClient.
	 *
	 * @param service
	 * @return the first ServiceInstance of a given service.
	 */
	private ServiceInstance getFirstServiceInstance(String service){

		if(StringUtils.isBlank(service) || discoveryClient.getInstances(service).isEmpty()){
			return null;
		} else {
			return discoveryClient.getInstances(service).get(0);
		}
	}
    
}
