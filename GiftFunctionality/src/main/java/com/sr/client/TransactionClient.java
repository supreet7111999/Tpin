package com.sr.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.sr.dto.AvailGiftCard;
import com.sr.dto.DeductGiftCardCharges;

@FeignClient(name = "TRANSACTION-SERVICE")
public interface TransactionClient {
	@PostMapping("transaction/deductGiftCardCharges")
	public ResponseEntity<?> deductGiftCardCharges(@RequestBody DeductGiftCardCharges deductGiftCardCharges);
	@PostMapping("transaction/availGiftCard")
	public ResponseEntity<?> availGiftCard(@RequestBody AvailGiftCard availGiftCard);
}
