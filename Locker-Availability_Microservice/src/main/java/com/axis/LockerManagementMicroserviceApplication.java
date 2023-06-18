package com.axis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class LockerManagementMicroserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(LockerManagementMicroserviceApplication.class, args);
	}
	  
//	@Bean
//	    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
//	        return new PropertySourcesPlaceholderConfigurer();
//	    }


}
