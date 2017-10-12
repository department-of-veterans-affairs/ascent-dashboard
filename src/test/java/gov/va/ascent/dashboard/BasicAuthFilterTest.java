package gov.va.ascent.dashboard;

import com.fasterxml.jackson.core.Base64Variants;
import com.netflix.zuul.context.RequestContext;
import org.junit.Before;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class BasicAuthFilterTest {



    @Before
    public void setTestRequestcontext() throws Exception {

        RequestContext context = new RequestContext();
        RequestContext.testSetCurrentContext(context);
    }

    @Test
    public void basicAuthFilterTest() throws Exception{
        BasicAuthFilter basicAuthFilter = new BasicAuthFilter();

        assertEquals(true, basicAuthFilter.shouldFilter());
        assertEquals(0,basicAuthFilter.filterOrder());
        assertEquals("pre", basicAuthFilter.filterType());

    }

    @Test
    public void basicAuthFilterTestRun() throws Exception{
        BasicAuthFilter basicAuthFilter = new BasicAuthFilter();
        basicAuthFilter.setPassword("Password");
        basicAuthFilter.setUsername("Username");
        basicAuthFilter.afterPropertiesSet();
        String auth = "Username:Password";
        String encodedAuthExpected = "Basic "
                + Base64Variants.MIME_NO_LINEFEEDS.encode(auth.getBytes(StandardCharsets.US_ASCII));
        basicAuthFilter.run();
        RequestContext ctx = RequestContext.getCurrentContext();
        Map<String,String> map = ctx.getZuulRequestHeaders();
        assertEquals(encodedAuthExpected, map.get("authorization"));

    }

}
