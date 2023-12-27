package com.bookingservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

import java.awt.desktop.SystemEventListener;

@SpringBootApplication
@EnableEurekaServer
@EnableDiscoveryClient
public class Application {

	public static void main(String[] args) {
		System.out.println("---------------------------");
		System.getenv("SPRING_DATASOURCE_PASSWORD");
		SpringApplication.run(Application.class, args);

	}

}
