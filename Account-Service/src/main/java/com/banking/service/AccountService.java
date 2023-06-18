package com.banking.service;
import java.util.List;

import javax.security.auth.login.AccountNotFoundException;

import org.springframework.stereotype.Service;

import com.banking.dto.AccountDTO;
import com.banking.dto.TransactionDTO;
import com.banking.exceptions.InvalidAccessException;
import com.banking.exceptions.UserDoesNotExistsException;
import com.banking.model.Account;
import com.banking.model.AccountCreationStatus;
import com.banking.model.TransactionStatus;

@Service
public interface AccountService {

	AccountCreationStatus createAccount(AccountDTO accountDTO) throws InvalidAccessException;

	List<Account> getAllAccounts() throws InvalidAccessException;

	Account getAccountById(String id) throws UserDoesNotExistsException;

	Account getAccountByUserId(Long userId) throws UserDoesNotExistsException;

	TransactionStatus deposit(TransactionDTO transactionDTO) throws UserDoesNotExistsException;

	TransactionStatus withdraw(TransactionDTO transactionDTO) throws UserDoesNotExistsException;

	Account getAccountByAccountType(String accountType) throws UserDoesNotExistsException;

}
