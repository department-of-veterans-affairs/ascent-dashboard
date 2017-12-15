package gov.va.ascent.dashboard;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * An <tt>Ascent Dashboard Controller</tt> to generate and serve the misc. dashboard URL requests
 *
 */
@Controller
@RefreshScope
public class DashboardController {

	private static final Logger LOGGER = LoggerFactory.getLogger(DashboardController.class);

	private static final String REST_API = "REST-API";
	private static final String APP_TYPE = "appType";

	@Value("${kibana.url:http://localhost:5601}")
	private String kibanaUrl;

	@Value("${zipkin.url:http://localhost:8700/zipkin/}")
	private String zipkinUrl;

	@Value("${gateway.url:http://localhost:8762}")
    private String gatewayUrl;

	@Autowired
    private DiscoveryClient discoveryClient;

	@Autowired
	private GatewayRoutesClient gatewayRoutesClient;
	
	@RequestMapping("/")
    public String index() {
        return "index";
    }

	@RequestMapping("/zipkin")
	public void zipkin(HttpServletRequest request, HttpServletResponse response) throws IOException {
		if (zipkinUrl != null) {
			response.sendRedirect(zipkinUrl);
		}
	}

	@RequestMapping("/kibana")
	public void kibana(HttpServletRequest request, HttpServletResponse response) throws IOException {
		if (kibanaUrl != null) {
			response.sendRedirect(kibanaUrl); 
		}
	}

    @RequestMapping("/swagger-dash")
    public String swagger(Model model) {

    	final Map<String, List<String>> swaggerApps = new TreeMap<>();
		final Map<String, String> routes = getRoutes();
		LOGGER.error(Arrays.toString(routes.entrySet().toArray()));
    	discoveryClient.getServices().forEach((String service) -> {

			final ServiceInstance firstServiceInstance = getFirstServiceInstance(service);

			if(firstServiceInstance != null
					&& REST_API.equals(firstServiceInstance.getMetadata().get(APP_TYPE))){
			    routes.forEach((route, serviceId) -> {
			        if(StringUtils.equalsIgnoreCase(serviceId,firstServiceInstance.getServiceId())) {
                        if(swaggerApps.get(firstServiceInstance.getServiceId().toLowerCase()) == null){
                            swaggerApps.put(firstServiceInstance.getServiceId().toLowerCase(), new ArrayList<>());
                        }
                        swaggerApps.get(firstServiceInstance.getServiceId().toLowerCase())
                                .add(gatewayUrl + route + "swagger-ui.html");
                    }
                });
			}
		});

    	model.addAttribute("swaggerApps", swaggerApps);
    	model.addAttribute("currentPageTitle", "Swagger URLs");
        return "swagger";
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

    /**
     * Returns the routes available on Ascent Gateway
     *
     * @return map of the routes to service Id
     */
	private Map<String, String> getRoutes(){
	    final Map<String, String> uneditedRoutes = gatewayRoutesClient.getRoutes();
	    final Map<String, String> routes = new HashMap<>();
		uneditedRoutes.forEach((route, service) -> {
            final String routeWithoutAsterisk = route.replace("*", "");
            routes.put(routeWithoutAsterisk, service);
        });
	    return routes;
    }
    
}
