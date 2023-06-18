package com.banking.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.banking.dto.Account;
import com.banking.dto.TransactionDTO;
import com.banking.dto.TransactionStatus;

@FeignClient(name="ACCOUNT-SERVICE")
public interface AccountClient {
	
	@PostMapping("account/deposit")
	public TransactionStatus deposit(@RequestBody TransactionDTO transactionDTO);
	
	@PostMapping("account/withdraw")
	public TransactionStatus withdraw(@RequestBody TransactionDTO transactionDTO);
	
	@GetMapping("account/get-accounts-id/{accountId}")
	public Account getAccounts(@PathVariable String accountId);

	@GetMapping("account/get-accounts/{accountType}")
	public Account getAccountBytype(@PathVariable String accountType);


}