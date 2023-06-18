package com.sr.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.sr.dto.TransactionDTO;
import com.sr.dto.TransactionStatus;
import com.sr.dto.AccountDTO;


@FeignClient(name="ACCOUNT-SERVICE")
public interface AccountClient {

	@GetMapping("/account/get-customer-accounts/{userId}")
	AccountDTO getCustomerAccounts(@PathVariable Long userId);
	
	@PostMapping("account/deposit")
	TransactionStatus deposit(@RequestBody TransactionDTO transactionDTO);
}
