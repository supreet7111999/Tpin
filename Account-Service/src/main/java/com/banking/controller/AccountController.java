package com.banking.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

import javax.security.auth.login.AccountNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.banking.dto.AccountDTO;
import com.banking.dto.TransactionDTO;
import com.banking.exceptions.InvalidAccessException;
import com.banking.exceptions.UserDoesNotExistsException;
import com.banking.model.Account;
import com.banking.model.AccountCreationStatus;
import com.banking.model.TransactionStatus;
import com.banking.service.AccountService;
import com.banking.service.StatementService;

import feign.FeignException.FeignClientException;

@RestController
@RequestMapping("/account")
public class AccountController {
	
	@Autowired
	AccountService accountService;
	
	@Autowired
	StatementService statementService;
	
	
	@PostMapping("/create-account")
	public ResponseEntity<?> createAccount(@RequestBody AccountDTO accountDTO) throws InvalidAccessException {
		
			AccountCreationStatus creationStatus = accountService.createAccount(accountDTO);
			if(creationStatus.getMessage().equalsIgnoreCase("INVALID_REQUEST")) {				
				return new ResponseEntity<>(creationStatus,HttpStatus.BAD_REQUEST);
			}
			
			return new ResponseEntity<>(creationStatus,HttpStatus.OK);
		
	}
	
	@GetMapping("/get-accounts-id/{accountId}")
	public ResponseEntity<?> getAccounts(@PathVariable String accountId) throws UserDoesNotExistsException {
		
			return new ResponseEntity<>(accountService.getAccountById(accountId),HttpStatus.OK);
		
	}
	@GetMapping("/get-accounts/{accountType}")
	public ResponseEntity<?> getAccountBytype(@PathVariable String accountType) throws UserDoesNotExistsException {
		
			return new ResponseEntity<>(accountService.getAccountByAccountType(accountType),HttpStatus.OK);
		
	}
	

	@GetMapping("/get-customer-accounts/{userId}")
	public ResponseEntity<?> getCustomerAccounts(@PathVariable Long userId) throws UserDoesNotExistsException {
	
			return new ResponseEntity<>(accountService.getAccountByUserId(userId),HttpStatus.OK);
		
	}
	

//	@GetMapping("/get-my-account")
//	public ResponseEntity<?> getMyAccount(@RequestHeader(name = "Authorization")String token){
//		try {
//			Account account = accountService.getMyAccount(token);
//			return new ResponseEntity<>(account,HttpStatus.OK);
//		} catch (InvalidAccessException e) {
//			return new ResponseEntity<>("UNAUTHORIZED_ACCESS",HttpStatus.UNAUTHORIZED);
//		} catch(FeignClientException e) {
//			String[] message = e.getMessage().split(" ");
//			int errCode = Integer.parseInt(message[0].split("")[1]+message[0].split("")[2]+message[0].split("")[3]);
//			return new ResponseEntity<>(message[5],HttpStatus.valueOf(errCode));
//		}
//	}
	
	@GetMapping("/getAccountStatement/{accountId}/{fromDate}/{toDate}")
	public ResponseEntity<?> getAccountStatement(@PathVariable String fromDate, @PathVariable String toDate, @PathVariable String accountId){
		try {
			return new ResponseEntity<>(statementService.getAllStatements(fromDate, toDate, accountId),HttpStatus.OK);
		} catch (ParseException e) {
			return new ResponseEntity<>("DATE_MUST_BE_OF_PATTERN_YYYY-MM-DD",HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/getAccountStatement/{accountId}")
	public ResponseEntity<?> getAccountStatement(@PathVariable String accountId){
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		int currentMonth = LocalDate.now().getMonthValue();
		int currentYear = LocalDate.now().getYear();
		int today = LocalDate.now().getDayOfMonth();
		calendar.set(currentYear, currentMonth,today);
		int firstDay = calendar.getActualMinimum(Calendar.DAY_OF_MONTH);
		int lastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		try {
			Date fromDate = simpleDateFormat.parse(currentYear+"-"+currentMonth+"-"+firstDay);
			Date toDate = simpleDateFormat.parse(currentYear+"-"+currentMonth+"-"+lastDay);
			return new ResponseEntity<>(statementService.getAllStatements(fromDate, toDate,accountId),HttpStatus.OK);
		} catch (ParseException e) {
			return new ResponseEntity<>("OOPS_SOMETHING_WENT_WRONG",HttpStatus.METHOD_NOT_ALLOWED);
		}
	}

	@PostMapping("/deposit")
	public ResponseEntity<?> deposit(@RequestBody TransactionDTO transactionDTO) throws UserDoesNotExistsException{
	
			TransactionStatus transactionStatus = accountService.deposit(transactionDTO);
			return new ResponseEntity<>(transactionStatus,HttpStatus.OK);
		
	}
	

	@PostMapping("/withdraw")
	public ResponseEntity<?> withdraw(@RequestBody TransactionDTO transactionDTO) throws UserDoesNotExistsException{
			TransactionStatus transactionStatus = accountService.withdraw(transactionDTO);
			return new ResponseEntity<>(transactionStatus,HttpStatus.OK);
		
	}
	
}