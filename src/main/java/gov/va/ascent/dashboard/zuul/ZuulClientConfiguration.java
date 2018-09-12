package gov.va.ascent.dashboard.zuul;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.cloud.netflix.zuul.filters.ProxyRequestHelper;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.cloud.netflix.zuul.filters.route.SimpleHostRoutingFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ZuulClientConfiguration {

	@Bean
	public HttpClient httpClient() {
		return HttpClients.custom()
				.useSystemProperties()
				.setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
				.build();
	}

	/**
	 * override simpleHost routing filter to autowire our custom httpclient
	 */
	@Bean
	public SimpleHostRoutingFilter simpleHostRoutingFilter(ProxyRequestHelper helper, ZuulProperties zuulProperties,
			CloseableHttpClient httpClient) {
		return new SimpleHostRoutingFilter(helper, zuulProperties, httpClient);
	}

}
