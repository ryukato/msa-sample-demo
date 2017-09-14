package com.msa.cqrs.gatewayservice;

import com.msa.cqrs.gatewayservice.prefilters.SimpleLoggingPreFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;

@EnableZuulProxy // Acts as reverse proxy, forwarding requests to other services based on routes.
@SpringBootApplication
public class GatewayServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayServiceApplication.class, args);
	}

	@Bean
	public SimpleLoggingPreFilter simplePreFilter() {
		return new SimpleLoggingPreFilter();
	}
}
