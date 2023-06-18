package com.banking.service;


import java.util.List;

import org.springframework.stereotype.Service;

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

@Service
public interface TransactionService {

	TransactionStatus deposit(OneWayTransactionDTO transactionDTO) throws InvalidAccessException;

	TransactionStatus withdraw(OneWayTransactionDTO transactionDTO)
			throws InvalidAccessException, InsufficientBalanceException;

	TransferSuccessMessage transfer(TransferDTO transferDTO)
			throws InvalidAccessException, InsufficientBalanceException;

	List<TransactionHistory> transactionHistory(String accountId)
			throws InvalidAccessException, InvalidAccountIdException;

	TransferSuccessMessage deductLockerCharges(DeductLockerChargesRequest deductLockerChargesRequest)
			throws InvalidAccessException, InsufficientBalanceException;

	GiftTransactionStatus deductGiftCardCharges(DeductGiftCardCharges deductGiftCardCharges)
			throws InvalidAccessException, InsufficientBalanceException;
	
	GiftTransactionStatus availGiftCard(AvailGiftCard availGiftCard) 
			throws InvalidAccessException;
	
	TransferSuccessMessage deductCreditCardCharges(DeductCreditCardChargesRequest deductCreditCardChargesRequest ) 
			throws InvalidAccessException, InsufficientBalanceException;

}