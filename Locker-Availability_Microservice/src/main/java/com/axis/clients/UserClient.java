package com.axis.clients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.axis.dto.UserDTO;

@FeignClient(name="LOGIN-FUNCTIONALITY")
public interface UserClient {
	
	@GetMapping("/employee/{Id}")
	UserDTO getUser(@PathVariable("Id") Long userID);

	
	@GetMapping("/employee/mark-account-created/{userId}")
	public boolean markAccountAsCreated(@PathVariable("userId") Long userId);


//	@RequestMapping(value="/employee/mark-account-created/{customerId}",method = {RequestMethod.GET})
//	public boolean markAccountAsCreated(@PathVariable Long userId);


}