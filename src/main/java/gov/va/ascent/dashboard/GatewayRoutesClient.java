package gov.va.ascent.dashboard;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;


@FeignClient(value="ascent-gateway", configuration = FeignClientConfiguration.class)
@FunctionalInterface
public interface GatewayRoutesClient {

    @RequestMapping(value = "/actuator/routes", method = RequestMethod.GET)
    Map<String, String> getRoutes();

}
