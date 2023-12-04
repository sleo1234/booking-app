package com.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient

public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}


	@Bean
	public RouteLocator routeLocator(RouteLocatorBuilder builder) {


		String host="**.localhost:39091";
		String path = "/booking-service";
		String uri = "http://localhost:39091";
		 return builder.routes()
				.route(p -> p
						.path(path)

						.uri(uri))
				.build();
	}

}
