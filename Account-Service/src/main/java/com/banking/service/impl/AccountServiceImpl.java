package com.banking.service.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;


import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//import com.banking.clients.AuthClient;
import com.banking.clients.UserClient;
import com.banking.clients.MailClient;
import com.banking.clients.TransactionClient;
import com.banking.dto.AccountDTO;
import com.banking.dto.EmailRequest;
import com.banking.dto.UserDTO;
import com.banking.dto.OneWayTransactionDTO;
import com.banking.dto.StatementDTO;
import com.banking.dto.TransactionDTO;
import com.banking.exceptions.InvalidAccessException;
import com.banking.exceptions.UserDoesNotExistsException;
import com.banking.model.Account;
import com.banking.model.AccountCreationStatus;
import com.banking.model.TransactionStatus;
import com.banking.repository.AccountRepository;
import com.banking.service.AccountService;
import com.banking.service.StatementService;

@Service
public class AccountServiceImpl implements AccountService {
	
	@Autowired
	AccountRepository accountRepository;

//	@Autowired
//	AuthClient authClient;

	@Autowired
	MailClient mailClient;
	
	@Autowired
	StatementService statementService;
	
	@Autowired
	TransactionClient transactionClient;

	@Autowired
	UserClient userClient;

	@Override
	public AccountCreationStatus createAccount(AccountDTO accountDTO) throws InvalidAccessException {
	    UserDTO user = userClient.getUser(accountDTO.getUserId());
	    if(user.isAccountCreated()) {
	    	throw new InvalidAccessException("Account already exists");
	    }
	    
	    long count = accountRepository.count();
//	    System.out.println(count);
	    String id = "ACC" + String.valueOf(count);
	    if (accountDTO.getAccountType().equalsIgnoreCase("SAVINGS") && accountDTO.getDeposit() >= 400) {
	       
	    	accountRepository.save(new Account(id, accountDTO.getAccountType(), user.getId(), 0));
	       
	        userClient.markAccountAsCreated(accountDTO.getUserId());
			/* problem yha hai */

	        TransactionStatus deposit = transactionClient.deposit(new OneWayTransactionDTO(id, "NEW_ACCOUNT_OPEN", "CASH", accountDTO.getDeposit()));
	        if(deposit!=null) {
	        	return new AccountCreationStatus(accountDTO.getAccountType() + "_ACCOUNT_CREATED", id);
	        }
	        else {
	        	throw new InvalidAccessException("Error while calling transactionclient deposit in account");
	        }
	    } else if (accountDTO.getAccountType().equalsIgnoreCase("CURRENT") && accountDTO.getDeposit() >= 1000) {
	        accountRepository.save(new Account(id, accountDTO.getAccountType(), user.getId(), 0));
	        userClient.markAccountAsCreated(accountDTO.getUserId());
	        transactionClient.deposit(new OneWayTransactionDTO(id, "NEW_ACCOUNT_OPEN", "CASH", accountDTO.getDeposit()));
	        return new AccountCreationStatus(accountDTO.getAccountType() + "_ACCOUNT_CREATED", id);
	    } else if (accountDTO.getAccountType().equalsIgnoreCase("ZERO BALANCE")) {
	        accountRepository.save(new Account(id, accountDTO.getAccountType(), user.getId(), 0));
	        userClient.markAccountAsCreated(accountDTO.getUserId());
	        if (accountDTO.getDeposit() > 0) {
	            transactionClient.deposit(new OneWayTransactionDTO(id, "NEW_ACCOUNT_OPEN", "CASH", accountDTO.getDeposit()));
	        }
	        return new AccountCreationStatus(accountDTO.getAccountType() + "_ACCOUNT_CREATED", id);
	    } 
		else if (accountDTO.getAccountType().equalsIgnoreCase("BANK_ACCOUNT")) {
			accountRepository.save(new Account(id, accountDTO.getAccountType(), user.getId(), 0));
			userClient.markAccountAsCreated(accountDTO.getUserId());
			if (accountDTO.getDeposit() > 0) {
				transactionClient
						.deposit(new OneWayTransactionDTO(id, "BANK_ACCOUNT_OPEN", "CASH", accountDTO.getDeposit()));
			}
			return new AccountCreationStatus(accountDTO.getAccountType() + "BANK_ACCOUNT_CREATED", id);
		}else {
	        throw new InvalidAccessException("INVALID_REQUEST");
	    }
	}

	@Override
	public List<Account> getAllAccounts() throws InvalidAccessException {
	    return accountRepository.findAll();
	}

	@Override
	public Account getAccountById(String id) throws UserDoesNotExistsException {
	    Optional<Account> account = accountRepository.findById(id);
	    if (account.isPresent()) {
	        return account.get();
	    }
	    throw new UserDoesNotExistsException();
	}


	@Override
	public Account getAccountByUserId(Long id) throws UserDoesNotExistsException {
	    Optional<Account> account = accountRepository.findByUserId(id);
	    if (account.isPresent()) {
	        return account.get();
	    }
	    throw new UserDoesNotExistsException();
	}


	@Override
	public TransactionStatus withdraw(TransactionDTO transactionDTO) throws UserDoesNotExistsException {
	    Optional<Account> account = accountRepository.findById(transactionDTO.getAccountId());
	    
	    LocalDateTime currentTime = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String formattedTime = currentTime.format(formatter);
		long userID = account.get().getUserId();
	    if (account.isPresent()) {

	        Account customerAccount = account.get();
	        Long userId = customerAccount.getUserId();

	        double balance = customerAccount.getBalance() - transactionDTO.getAmount();
	        customerAccount.setBalance(balance);
	        accountRepository.save(customerAccount);

	        statementService.writeStatement(
	                new StatementDTO(new Date(), transactionDTO.getAccountId(), transactionDTO.getNarration(),
	                        transactionDTO.getRefNo(), transactionDTO.getAmount(), 0, balance));
         	UserDTO user = userClient.getUser(userId);
         	String supportContact="+91 9935737136";
	        String message = "Dear " + user.getName() + "," + "\n\n"
	                + "We would like to inform you that an amount of " + transactionDTO.getAmount() + " has been Debited from your account. " + "\n"
	                + "Account ID: " + transactionDTO.getAccountId() + "\n"
	                + "Transaction ID: " + transactionDTO.getRefNo() + "\n"
	                + "Current balance: " + balance + "\n"
	                + "Date: " + formattedTime + "\n"
	                + "Narration: " + transactionDTO.getNarration() + "\n\n"
	                + "For any concerns regarding this transaction, please contact our customer support at " + supportContact + ".\n"
	                + "Thank you for choosing ABC BANK. We are here to assist you.\n\n"
	                + "Regards,\n"
	                + "ABC BANK";
	        
	    	EmailRequest emailRequest= new EmailRequest(user.getEmail(),"Debit Notification From ABC BANK",message);
	    	
	    	System.out.println("email sent demo :"+ emailRequest);
//	    	mailClient.sendEmail(emailRequest);
	    	
	    	mailClient.sendEmail(emailRequest);
	    	
	    	   System.out.println( new TransactionStatus("DEPOSIT_SUCCESSFULL_INTO_" + transactionDTO.getAccountId(), balance));

		        

	        return new TransactionStatus("WITHDRAW_SUCCESSFULL_FROM_" + transactionDTO.getAccountId(), balance  );
	    }
	    
	    throw new UserDoesNotExistsException();
	}


//	@Override
//	public TransactionStatus deposit(TransactionDTO transactionDTO) throws UserDoesNotExistsException {
//	    Optional<Account> account = accountRepository.findById(transactionDTO.getAccountId());
//	    LocalDateTime currentTime = LocalDateTime.now();
//		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//		String formattedTime = currentTime.format(formatter);
//	    if (account.isPresent()) {
//	        Account customerAccount = account.get();
//	        double balance = customerAccount.getBalance() + transactionDTO.getAmount();
//	        customerAccount.setBalance(balance);
//	        accountRepository.save(customerAccount);
//	        statementService.writeStatement(
//	                new StatementDTO(new Date(), transactionDTO.getAccountId(), transactionDTO.getNarration(),
//	                        transactionDTO.getRefNo(), 0, transactionDTO.getAmount(), balance));
//	        Long userId = customerAccount.getUserId();
//         	UserDTO user = userClient.getUser(userId);
//         	String supportContact="+91 9935737136";
//	        String message = "Dear " + user.getName() + "," + "\n\n"
//	                + "We would like to inform you that an amount of " + transactionDTO.getAmount() + " has been credited from your account. " + "\n"
//	                + "Account ID: " + transactionDTO.getAccountId() + "\n"
//	                + "Transaction ID: " + transactionDTO.getRefNo() + "\n"
//	                + "Current balance: " + balance + "\n"
//	                + "Date: " + formattedTime + "\n"
//	                + "Narration: " + transactionDTO.getNarration() + "\n\n"
//	                + "For any concerns regarding this transaction, please contact our customer support at " + supportContact + ".\n"
//	                + "Thank you for choosing ABC BANK. We are here to assist you.\n\n"
//	                + "Regards,\n"
//	                + "ABC BANK";
//	        
//	    	EmailRequest emailRequest= new EmailRequest(user.getEmail(),"Credit Notification From ABC BANK",message);
//	    	System.out.println("email sent demo :"+ emailRequest);
//
//	    	mailClient.sendEmail(emailRequest);
//	        return new TransactionStatus("DEPOSIT_SUCCESSFULL_INTO_" + transactionDTO.getAccountId(), balance);
//	    }
//	    throw new UserDoesNotExistsException();
//	}
	@Override
	public TransactionStatus deposit(TransactionDTO transactionDTO) throws UserDoesNotExistsException {
	    Optional<Account> account = accountRepository.findById(transactionDTO.getAccountId());
	    LocalDateTime currentTime = LocalDateTime.now();
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	    String formattedTime = currentTime.format(formatter);
	    if (account.isPresent()) {
	        Account customerAccount = account.get();
	        double balance = customerAccount.getBalance() + transactionDTO.getAmount();
	        customerAccount.setBalance(balance);
	        accountRepository.save(customerAccount);
	        statementService.writeStatement(
	                new StatementDTO(new Date(), transactionDTO.getAccountId(), transactionDTO.getNarration(),
	                        transactionDTO.getRefNo(), 0, transactionDTO.getAmount(), balance));
	        Long userId = customerAccount.getUserId();
	        UserDTO user = userClient.getUser(userId);
	          System.out.println( new TransactionStatus("DEPOSIT_SUCCESSFULL_INTO_" + transactionDTO.getAccountId(), balance));


	        if (user != null) {
	            String supportContact = "+91 9935737136";
	            String message = "Dear " + user.getName() + "," + "\n\n"
	                    + "We would like to inform you that an amount of " + transactionDTO.getAmount() + " has been credited from your account. " + "\n"
	                    + "Account ID: " + transactionDTO.getAccountId() + "\n"
	                    + "Transaction ID: " + transactionDTO.getRefNo() + "\n"
	                    + "Current balance: " + balance + "\n"
	                    + "Date: " + formattedTime + "\n"
	                    + "Narration: " + transactionDTO.getNarration() + "\n\n"
	                    + "For any concerns regarding this transaction, please contact our customer support at " + supportContact + ".\n"
	                    + "Thank you for choosing ABC BANK. We are here to assist you.\n\n"
	                    + "Regards,\n"
	                    + "ABC BANK";
	            
	            EmailRequest emailRequest = new EmailRequest(user.getEmail(), "Credit Notification From ABC BANK", message);
	            System.out.println("email sent demo: " + emailRequest);
	            mailClient.sendEmail(emailRequest);
	          System.out.println( new TransactionStatus("DEPOSIT_SUCCESSFULL_INTO_" + transactionDTO.getAccountId(), balance));

	        
	        }
	        else {
	            // Handle the case when the user is null
	            // For example, you can log an error or throw an exception
	            // Logging an error message:
	            System.err.println("User not found for userId: " + userId);
	        }
	        return new TransactionStatus("DEPOSIT_SUCCESSFULL_INTO_" + transactionDTO.getAccountId(), balance);


	    }
	    throw new UserDoesNotExistsException();
	}


	@Override
	public Account getAccountByAccountType(String accountType) throws UserDoesNotExistsException {
		Optional<Account> account = accountRepository.findByAccountType(accountType);
		if (account.isPresent()) {
			return account.get();
		}
		throw new UserDoesNotExistsException();
	}


}