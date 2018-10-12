package gov.va.ascent.dashboard;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = { "kibana.url=#{null}", "zipkin.url=#{null}" })
@AutoConfigureMockMvc
public class DashboardControllerPropertyTest extends BaseTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser
    public void kibanaTest() throws Exception {
        this.mockMvc.perform(get("/kibana"))
                .andExpect(redirectedUrl(null));
    }
    
    @Test
    @WithMockUser
    public void zipkinTest() throws Exception {
        this.mockMvc.perform(get("/zipkin"))
                .andExpect(redirectedUrl(null));
    }    
}
