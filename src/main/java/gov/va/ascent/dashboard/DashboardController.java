package gov.va.ascent.dashboard;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
 * An <tt>Ascent Dashboard Controller</tt> to generate and serve the misc. dasboard URL requests
 *
 */
@Controller
public class DashboardController {
	
	private final static Logger LOGGER = LoggerFactory.getLogger(DashboardController.class);
	
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
    	ServiceInstance zuulInstance = getZuul();
    	
    	List<SwaggerApp> swaggerApps = new ArrayList<SwaggerApp>();
    	discoveryClient.getServices().forEach((String service) -> {
			//use discovery service to build out a collection of our ServiceInstanceDetail objects
			discoveryClient.getInstances(service).forEach((ServiceInstance serviceInstance) -> {
				if("REST-API".equals(serviceInstance.getMetadata().get("appType"))){
					SwaggerApp swaggerApp = new SwaggerApp();
					swaggerApp.setApp(serviceInstance.getServiceId().toLowerCase());
					swaggerApp.setUrl(
							ServletUriComponentsBuilder.fromUri(zuulInstance.getUri())
							.path("api/")
							.path(serviceInstance.getServiceId().toLowerCase())
							.path("/swagger-ui.html")
							.build().toUriString());
					swaggerApps.add(swaggerApp);
				}
			});
		});
    	model.addAttribute("swaggerApps", swaggerApps);
        return "swagger";
    }
    
    @RequestMapping("/monitor-dash")
    public String monitor(Model model) {
    	ServiceInstance zuulInstance = getZuul();
    	
    	List<String> gatewayUrls = new ArrayList<String>();
    	discoveryClient.getServices().forEach((String service) -> {
			//use discovery service to build out a collection of our ServiceInstanceDetail objects
			discoveryClient.getInstances(service).forEach((ServiceInstance serviceInstance) -> {
				if("REST-API".equals(serviceInstance.getMetadata().get("appType"))){
					gatewayUrls.add(ServletUriComponentsBuilder.fromUri(zuulInstance.getUri())
						.path("api/")
						.path(serviceInstance.getServiceId().toLowerCase())
						.build().toUriString());
				}
			});
		});
    	model.addAttribute("gatewayUrls", gatewayUrls);
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
    
    class SwaggerApp {
    	private String app;
    	private String url;
    	public String getApp() {
			return app;
		}
		public void setApp(String app) {
			this.app = app;
		}
		public String getUrl() {
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}
    }
    
}
