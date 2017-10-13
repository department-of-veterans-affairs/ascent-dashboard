package gov.va.ascent.dashboard;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

/**
 * An <tt>Ascent Dashboard Controller</tt> to generate and serve the misc. dashboard URL requests
 *
 */
@Controller
public class DashboardController {

	private static final String REST_API = "REST-API";
	private static final String APP_TYPE = "appType";
	private static final String API = "api/";

	@Value("${kibana.url:http://localhost:5601}")
	private String kibanaUrl;

	@Value("${zipkin.url:http://localhost:8700}")
	private String zipkinUrl;

	@Autowired
    private DiscoveryClient discoveryClient;
	
	@RequestMapping("/")
    public String index() {
        return "index";
    }

	@RequestMapping("/zipkin")
	public void zipkin(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.sendRedirect(zipkinUrl);
	}

	@RequestMapping("/kibana")
	public void kibana(HttpServletRequest request, HttpServletResponse response) throws IOException {

		response.sendRedirect(kibanaUrl);
	}

    @RequestMapping("/swagger-dash")
    public String swagger(Model model) {
    	final ServiceInstance zuulInstance = getServiceInstance("ascent-gateway");

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

	private ServiceInstance getServiceInstance(final String serviceName){
		ServiceInstance desiredServiceInstance = null;

		for(ServiceInstance serviceInstance: discoveryClient.getInstances(serviceName)){
			desiredServiceInstance = serviceInstance;
			break;
		}
		return desiredServiceInstance;
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
