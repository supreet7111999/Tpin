package com.axis.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.axis.dto.DeductLockerChargesRequest;

@FeignClient(name = "TRANSACTION-SERVICE")
public interface TransactionClient {

	@PostMapping("/transaction/locker-charge")
	public ResponseEntity<?> lockerCharge(@RequestBody DeductLockerChargesRequest deductLockerCharges);
}