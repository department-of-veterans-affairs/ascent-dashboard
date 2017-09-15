package gov.va.ascent.dashboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * An <tt>Ascent Dashboard Application</tt> enabled for Spring Boot Application, 
 * Spring Cloud Netflix Hystrix circuit breakers, Hystrix Dashboard and Turbine.
 *
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableHystrix
@EnableHystrixDashboard
@Import(TurbineConfiguration.class)
public class AscentDashboardApplication {

	public static void main(String[] args) {
		SpringApplication.run(AscentDashboardApplication.class, args);
	}
	
	@Configuration
	@ConditionalOnProperty(name = "security.basic.enabled", havingValue = "true", matchIfMissing = true)
	@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
	protected static class ApplicationSecurity extends WebSecurityConfigurerAdapter {
		@Override
		public void configure(WebSecurity web) throws Exception {
		    web.ignoring().antMatchers("/turbine.stream");
		}
	}
}
