package com.banking.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.banking.dto.GiftTransactionStatus;
import com.banking.dto.OneWayTransactionDTO;
import com.banking.dto.TransactionStatus;
import com.banking.dto.TransferDTO;
import com.banking.dto.TransferSuccessMessage;
import com.banking.exception.InsufficientBalanceException;
import com.banking.exception.InvalidAccessException;
import com.banking.exception.InvalidAccountIdException;
import com.banking.model.AvailGiftCard;
import com.banking.model.DeductCreditCardChargesRequest;
import com.banking.model.DeductGiftCardCharges;
import com.banking.model.DeductLockerChargesRequest;
import com.banking.model.TransactionHistory;
import com.banking.service.TransactionService;

@RestController
@RequestMapping("/transaction")
public class TransactionController {
	
	@Autowired
	TransactionService service;
	
	@PostMapping("/deposit")
	public ResponseEntity<?> deposit(@RequestBody OneWayTransactionDTO transactionDTO) throws InvalidAccessException{

			TransactionStatus deposit = service.deposit(transactionDTO);
			return new ResponseEntity<>(deposit,HttpStatus.OK);
		
	}
	
	@PostMapping("/withdraw")
	public ResponseEntity<?> withdraw(@RequestBody OneWayTransactionDTO transactionDTO) throws InvalidAccessException, InsufficientBalanceException{
			TransactionStatus withdraw = service.withdraw(transactionDTO);
			return new ResponseEntity<>(withdraw,HttpStatus.OK);
		
	}
	
	@PostMapping("/transfer")
	public ResponseEntity<?> transfer(@RequestBody TransferDTO transferDTO) throws InvalidAccessException, InsufficientBalanceException{

			TransferSuccessMessage transfer = service.transfer(transferDTO);
			return new ResponseEntity<>(transfer,HttpStatus.OK);
		
	}
	
	@GetMapping("/get-transaction/{accountId}")
	public ResponseEntity<?> getTransactions(@PathVariable String accountId) throws InvalidAccessException, InvalidAccountIdException{
			List<TransactionHistory> transactionHistory = service.transactionHistory(accountId);
			return new ResponseEntity<>(transactionHistory,HttpStatus.OK);
	}
		

	@PostMapping("/locker-charge")
	public ResponseEntity<?> lockerCharge(@RequestBody DeductLockerChargesRequest deductLockerCharges) throws InvalidAccessException,InsufficientBalanceException{
		TransferSuccessMessage deduuctLockerCharge=service.deductLockerCharges(deductLockerCharges);
		return new ResponseEntity<>(deduuctLockerCharge, HttpStatus.OK);
	}
	
	@PostMapping("/deductGiftCardCharges")
    public ResponseEntity<?> deductGiftCardCharges(@RequestBody DeductGiftCardCharges deductGiftCardCharges) throws InvalidAccessException, InsufficientBalanceException{
    	GiftTransactionStatus deduuctGiftCardCharge=service.deductGiftCardCharges(deductGiftCardCharges);
    	return new ResponseEntity<>(deduuctGiftCardCharge, HttpStatus.OK);
    }
	@PostMapping("/availGiftCard")
    public ResponseEntity<?> availGiftCard(@RequestBody AvailGiftCard availGiftCard) throws InvalidAccessException{
    	GiftTransactionStatus deduuctGiftCardCharge=service.availGiftCard(availGiftCard);
    	return new ResponseEntity<>(deduuctGiftCardCharge, HttpStatus.OK);
    }
	
	@PostMapping("/credit-card-charge")
	public ResponseEntity<?> creditcardCharge(@RequestBody  DeductCreditCardChargesRequest deductCreditCardChargesRequest) throws InvalidAccessException,InsufficientBalanceException{
		TransferSuccessMessage deduuctCreditCardChargesRequest=service.deductCreditCardCharges(deductCreditCardChargesRequest);
		return new ResponseEntity<>(deduuctCreditCardChargesRequest, HttpStatus.OK);
	}

}