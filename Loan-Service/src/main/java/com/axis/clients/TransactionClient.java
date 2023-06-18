package com.axis.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.axis.dto.OneWayTransactionDTO;
import com.axis.exception.InvalidAccessException;
import com.axis.exception.InsufficientBalanceException;

@FeignClient(name = "TRANSACTION-SERVICE")
public interface TransactionClient {
	@PostMapping("/transaction/deposit")
	public ResponseEntity<?> deposit(@RequestBody OneWayTransactionDTO transactionDTO) throws InvalidAccessException;
	@PostMapping("/transaction/withdraw")
	public ResponseEntity<?> withdraw(@RequestBody OneWayTransactionDTO transactionDTO) throws InvalidAccessException, InsufficientBalanceException;
}
