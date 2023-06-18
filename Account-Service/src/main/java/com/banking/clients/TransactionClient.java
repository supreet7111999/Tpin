package com.banking.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.banking.dto.OneWayTransactionDTO;
import com.banking.model.TransactionStatus;

@FeignClient(name = "TRANSACTION-SERVICE")
public interface TransactionClient {

	@PostMapping("transaction/deposit")
	public TransactionStatus deposit(@RequestBody OneWayTransactionDTO transactionDTO);

}