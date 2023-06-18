package com.axis.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


import com.axis.dto.AccountDTO;


@FeignClient(name="ACCOUNT-SERVICE")
public interface AccountClient {

	@GetMapping("/account/get-customer-accounts/{userId}")
	AccountDTO getCustomerAccounts(@PathVariable Long userId);
	
}
