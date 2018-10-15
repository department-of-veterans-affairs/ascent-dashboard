package gov.va.ascent.dashboard;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.metrics.export.prometheus.EnablePrometheusMetrics;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import de.codecentric.boot.admin.config.EnableAdminServer;

/**
 * An <tt>Ascent Dashboard Application</tt> enabled for Spring Boot Application,
 * Spring Cloud Netflix Hystrix circuit breakers, Hystrix Dashboard and Turbine.
 *
 */
@Configuration
@EnableAutoConfiguration
@AutoConfigureAfter(SecurityAutoConfiguration.class)
@EnableDiscoveryClient
@EnableAdminServer
@SpringBootApplication
@EnableHystrix
@EnableHystrixDashboard
@EnableFeignClients
@EnablePrometheusMetrics
@Import(TurbineConfiguration.class)
public class AscentDashboardApplication extends WebSecurityConfigurerAdapter {

	@Autowired
	private SecurityProperties securityProperties;

	public static void main(final String[] args) {
		SpringApplication.run(AscentDashboardApplication.class, args);
	}

	@Autowired
	@Override
	protected void configure(final AuthenticationManagerBuilder auth) {
		addInMemoryAuthenticationProvider(auth);
	}

	@Override
	public void configure(final HttpSecurity http) throws Exception {

		http.httpBasic().and()
				.authorizeRequests()
				.antMatchers(HttpMethod.POST, "/api/applications").permitAll()//
				.antMatchers(HttpMethod.GET, "/actuator/health").permitAll()//
				.antMatchers(HttpMethod.HEAD, "/actuator/health").permitAll()//
				.antMatchers(HttpMethod.GET, "/actuator/info").permitAll()//
				.anyRequest().authenticated()//
				.and().csrf().ignoringAntMatchers("/api/**", "/manage/**")
				.csrfTokenRepository(csrfTokenRepository()).and()
				.addFilterAfter(csrfHeaderFilter(), CsrfFilter.class);
	}

	private void addInMemoryAuthenticationProvider(final AuthenticationManagerBuilder auth) {
		try {
			auth.inMemoryAuthentication()
					.withUser(securityProperties.getUser().getName())
					.password(securityProperties.getUser().getPassword())
					.roles(securityProperties.getUser().getRole().stream().toArray(String[]::new));
		} catch (final Exception ex) {
			throw new IllegalStateException("Cannot add InMemory users!", ex);
		}
	}

	private Filter csrfHeaderFilter() {
		return new RestOncePerRequestFilter();
	}

	private class RestOncePerRequestFilter extends OncePerRequestFilter {
		@Override
		protected void doFilterInternal(final HttpServletRequest request,
				final HttpServletResponse response, final FilterChain filterChain)
				throws ServletException, IOException {
			final CsrfToken csrf = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
			if (csrf != null) {
				Cookie cookie = WebUtils.getCookie(request, "XSRF-TOKEN");
				final String token = csrf.getToken();
				if (cookie == null || token != null && !token.equals(cookie.getValue())) {
					cookie = new Cookie("XSRF-TOKEN", token);
					cookie.setHttpOnly(true);
					cookie.setPath("/");
					response.addCookie(cookie);
				}
			}
			filterChain.doFilter(request, response);
		}
	}

	private CsrfTokenRepository csrfTokenRepository() {
		final HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
		repository.setHeaderName("X-XSRF-TOKEN");
		return repository;
	}

}
