package gov.va.ascent.dashboard;

import org.springframework.cloud.client.ServiceInstance;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is only for testing purposes.
 */
public class ServiceInstanceMockImpl implements ServiceInstance{

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
