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
public class CreditCardMicroserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CreditCardMicroserviceApplication.class, args);
	}
	
//	@Bean
//	protected RestTemplate getRestTemplate() {
//		return new RestTemplate();
//	}

//	  @Bean
//	    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
//	        return new PropertySourcesPlaceholderConfigurer();
//	    }

}
