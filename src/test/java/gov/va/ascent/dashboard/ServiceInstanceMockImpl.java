package gov.va.ascent.dashboard;

import de.codecentric.boot.admin.web.client.ApplicationOperations;
import de.codecentric.boot.admin.web.client.HttpHeadersProvider;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.mock;

/**
 * This class is only for testing purposes.
 */
@Configuration
public class ServiceInstanceMockImpl implements ServiceInstance{

    private RestTemplate restTemplate = mock(RestTemplate.class);
    private HttpHeadersProvider headersProvider = mock(HttpHeadersProvider.class);

    @Bean
    public ApplicationOperations getApplicationOperations(){
        return new ApplicationOperations(restTemplate, headersProvider);
    }

    final Map<String, String> metaData = new HashMap<>();

    String serviceId = "dummy-service";

    @Override
    public String getServiceId() {
        return serviceId;
    }

    @Override
    public String getHost() {
        return "localhost";
    }

    @Override
    public int getPort() {
        return 8080;
    }

    @Override
    public boolean isSecure() {
        return false;
    }

    @Override
    public URI getUri() {
        URI uri = null;
        try {
            uri = new URI("http://localhost");
        } catch (URISyntaxException e){
            //unit test so do nothing;
        }
        return uri;
    }

    @Override
    public Map<String, String> getMetadata() {

        return metaData;
    }
}
