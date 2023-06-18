package com.sr.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.sr.dto.UserDTO;

@FeignClient(name="LOGIN-FUNCTIONALITY")
public interface UserClient {
	
	
	@GetMapping("/employee/{Id}")
	UserDTO getUser(@PathVariable("Id") Long userID);
}
