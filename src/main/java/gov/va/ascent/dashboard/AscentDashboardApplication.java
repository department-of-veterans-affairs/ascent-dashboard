package gov.va.ascent.dashboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.context.annotation.Import;

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
}
