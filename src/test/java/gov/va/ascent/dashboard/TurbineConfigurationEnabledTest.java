package gov.va.ascent.dashboard;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.netflix.turbine.TurbineProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {"turbine.enabled=true"})
@AutoConfigureMockMvc
public class TurbineConfigurationEnabledTest extends BaseTest {

    @Autowired
    ApplicationContext applicationContext;

    @Test
    @WithMockUser
    public void testConfig() throws Exception {

        System.out.println("TURBINE ENABLED: " + applicationContext.getEnvironment().getProperty("turbine.enabled"));
        TurbineProperties t = applicationContext.getBean(TurbineProperties.class);
        assertNotNull(t);

    }

}
