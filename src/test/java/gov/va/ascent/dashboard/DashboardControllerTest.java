package gov.va.ascent.dashboard;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class DashboardControllerTest {



    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DiscoveryClient discoveryClient;

    @MockBean
    private GatewayRoutesClient gatewayRoutesClient;

    final List<String> services = new ArrayList<>();

    final List<ServiceInstance> serviceInstancesPlatform = new ArrayList<>();

    @Before
    public void setUp(){

        // setup basic services that would be running on the platform
        services.add("ascent-gateway");
        services.add("ascent-dashboard");

        ServiceInstanceMockImpl gateway = new ServiceInstanceMockImpl();
        gateway.serviceId = "ascent-gateway";
        ServiceInstance gatewayMock = gateway;

        ServiceInstanceMockImpl dashboard = new ServiceInstanceMockImpl();
        dashboard.serviceId = "ascent-dashboard";
        ServiceInstance dashboardMock = dashboard;

        serviceInstancesPlatform.add(gatewayMock);

        serviceInstancesPlatform.add(dashboardMock);
    }

    @After
    public void cleanUp(){
        services.clear();
        serviceInstancesPlatform.clear();
    }

    @Test
    @WithMockUser
    public void indexTest() throws Exception {
        this.mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }

    @Test
    @WithMockUser
    public void kibanaTest() throws Exception {
        this.mockMvc.perform(get("/kibana"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost:5601"));
    }

    @Test
    @WithMockUser
    public void zipkinTest() throws Exception {
        this.mockMvc.perform(get("/zipkin"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost:8700/zipkin/"));
    }

    @Test
    @WithMockUser
    public void shouldReturnSwaggerDashboard() throws Exception {
        //setup api service and service instance
        services.add("dummy-service");

        ServiceInstanceMockImpl restServiceMock = new ServiceInstanceMockImpl();
        restServiceMock.metaData.put("appType", "REST-API");
        restServiceMock.serviceId = "dummy-service";

        final List<ServiceInstance> serviceInstancesDummyServices = new ArrayList<>();
        serviceInstancesDummyServices.add(restServiceMock);

        //setup expected response
        final Map<String, List<String>> swaggerApps = new TreeMap<>();
        List<String> urls = new ArrayList<>();
        urls.add("http://localhost:8762/api/dummy-service/swagger-ui.html");
        swaggerApps.put("dummy-service",urls);

        Map<String, String> routes = new HashMap<>();
        routes.put("/api/dummy-service/**", "dummy-service");


        when(discoveryClient.getServices()).thenReturn(services);
        when(discoveryClient.getInstances("dummy-service")).thenReturn(serviceInstancesDummyServices);
        when(discoveryClient.getInstances("ascent-gateway")).thenReturn(serviceInstancesPlatform);
        when(gatewayRoutesClient.getRoutes()).thenReturn(routes);

        this.mockMvc.perform(
                get("/swagger-dash"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("swaggerApps"))
                .andExpect(model().attribute("swaggerApps", swaggerApps))
                .andExpect(view().name("swagger"));
    }

    @Test
    @WithMockUser
    public void shouldReturnSwaggerDashboardMultipleRoutesPerApp() throws Exception {
        //setup api service and service instance
        services.add("dummy-service");

        ServiceInstanceMockImpl restServiceMock = new ServiceInstanceMockImpl();
        restServiceMock.metaData.put("appType", "REST-API");
        restServiceMock.serviceId = "dummy-service";

        final List<ServiceInstance> serviceInstancesDummyServices = new ArrayList<>();
        serviceInstancesDummyServices.add(restServiceMock);

        //setup expected response
        final Map<String, List<String>> swaggerApps = new TreeMap<>();
        List<String> urls = new ArrayList<>();
        urls.add("http://localhost:8762/api/dummy-service/swagger-ui.html");
        urls.add("http://localhost:8762/api/service/swagger-ui.html");
        swaggerApps.put("dummy-service",urls);

        Map<String, String> routes = new HashMap<>();
        routes.put("/api/dummy-service/**", "dummy-service");
        routes.put("/api/service/**", "dummy-service");


        when(discoveryClient.getServices()).thenReturn(services);
        when(discoveryClient.getInstances("dummy-service")).thenReturn(serviceInstancesDummyServices);
        when(discoveryClient.getInstances("ascent-gateway")).thenReturn(serviceInstancesPlatform);
        when(gatewayRoutesClient.getRoutes()).thenReturn(routes);

        this.mockMvc.perform(
                get("/swagger-dash"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("swaggerApps"))
                .andExpect(model().attribute("swaggerApps", swaggerApps))
                .andExpect(view().name("swagger"));
    }

    @Test
    @WithMockUser
    public void shouldReturnSwaggerDashboardNoLinks() throws Exception {
        //setup expected response object
        final Map<String, List<String>> swaggerApps = new TreeMap<>();

        when(discoveryClient.getServices()).thenReturn(services);
        List<ServiceInstance> emptyServiceInstances = new ArrayList<>();
        when(discoveryClient.getInstances("dummy-service")).thenReturn(emptyServiceInstances);
        when(discoveryClient.getInstances("ascent-gateway")).thenReturn(serviceInstancesPlatform);
        this.mockMvc.perform(
                get("/swagger-dash"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("swaggerApps"))
                .andExpect(model().attribute("swaggerApps", swaggerApps))
                .andExpect(view().name("swagger"));
    }

}
