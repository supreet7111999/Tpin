package com.banking.service.impl;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.banking.clients.AccountClient;
import com.banking.clients.MailClient;
import com.banking.clients.UserClient;
import com.banking.dto.Account;
import com.banking.dto.GiftTransactionStatus;
import com.banking.dto.OneWayTransactionDTO;
import com.banking.dto.TransactionDTO;
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
import com.banking.model.RuleStatus;
import com.banking.model.TransactionHistory;
import com.banking.repository.TransactionHistoryRepository;
import com.banking.service.RulesService;
import com.banking.service.TransactionService;
@Service
public class TransactionServiceImpl implements TransactionService {
	
//	@Autowired
//	AuthClient authClient;

		
	@Autowired
	AccountClient accountClient;
	
	@Autowired
	MailClient mailClient;
	
	@Autowired
	UserClient userClient;
	
	@Autowired
	TransactionHistoryRepository repository;

	@Autowired
	RulesService rulesService;

	private boolean isTransactionValid(String accountType, double balance) throws InvalidAccessException {
		RuleStatus minBalance = rulesService.evaluateMinBalance(accountType, balance);
		if (minBalance.getStatus().equalsIgnoreCase("Allowed")) {
			return true;
		}
		return false;
	}

	@Override
	public TransactionStatus deposit(OneWayTransactionDTO transactionDTO) throws InvalidAccessException {
		long count = repository.count();
		String id = "TRANSACT" + String.valueOf(count); 
	    String narration = transactionDTO.getNarration();
	    if (narration.isBlank() || narration.isEmpty()) {
	        narration = "SELF_DEPOSIT";
	    }
	    
	    
		/* problem yha hai */
	    
	    TransactionStatus deposit = accountClient.deposit(
	            new TransactionDTO(transactionDTO.getAccountId(), narration, transactionDTO.getAmount(), id));

	    if (deposit != null) {
	    	repository.save(
	    			new TransactionHistory(id, transactionDTO.getAccountId(), transactionDTO.getAmount(), narration,
	    					transactionDTO.getTransactionType(), transactionDTO.getAccountId(), new Date(), "DEPOSIT"));
		    
	        return deposit;
	    } else {
	        throw new InvalidAccessException();
	    }
	    

	    
	}


	@Override
	public TransactionStatus withdraw(OneWayTransactionDTO transactionDTO)
	        throws InvalidAccessException, InsufficientBalanceException {

		
	    Account account = accountClient.getAccounts(transactionDTO.getAccountId());
	   
	    double amount = transactionDTO.getAmount();
	    
	    if (isTransactionValid(account.getAccountType(), account.getBalance() - amount)) {
	        String id = "TRANSACT" + repository.count();
	        String narration = transactionDTO.getNarration();
	        if (narration.isBlank() || narration.isEmpty()) {
	            narration = "SELF_WITHDRAW";
	        }
	        
	        
	        TransactionStatus withdraw = accountClient.withdraw(
	                new TransactionDTO(transactionDTO.getAccountId(), narration, amount, id));
	     
	        	
	        if (withdraw != null) {
	        	repository.save(new TransactionHistory(id, transactionDTO.getAccountId(),
	        			Math.round(amount * 100) / 100.0, narration, transactionDTO.getTransactionType(),
	        			transactionDTO.getAccountId(), new Date(), "WITHDRAW"));
	        	
	            return withdraw;
	        } else {
	            throw new InvalidAccessException();
	        }
	    }
	    throw new InsufficientBalanceException();
	}


	@Override
	public TransferSuccessMessage transfer(TransferDTO transferDTO)
	        throws InvalidAccessException, InsufficientBalanceException {
	    Account fromAccount = accountClient.getAccounts(transferDTO.getFromAccountId());
	   
	    Account toAccount = accountClient.getAccounts(transferDTO.getToAccountId());
	    double amount = transferDTO.getAmount();
	    if (isTransactionValid(fromAccount.getAccountType(), fromAccount.getBalance() - amount)) {
	        String id = "TRANSACT" + repository.count();
	        String narration = transferDTO.getNarration();
	        if (narration.isBlank() || narration.isEmpty()) {
	            narration = "SELF_TRANSFER";
	        }
	        TransactionStatus withdraw = accountClient.withdraw(
	                new TransactionDTO(transferDTO.getFromAccountId(), narration, amount, id));
	        TransactionStatus deposit = accountClient.deposit(
	                new TransactionDTO(transferDTO.getToAccountId(), narration, transferDTO.getAmount(), id));

	        if (withdraw != null && deposit != null) {
	        repository.save(new TransactionHistory(id, transferDTO.getFromAccountId(), transferDTO.getAmount(),
	                narration, transferDTO.getTransactionType(), transferDTO.getToAccountId(), new Date(),
	                "TRANSFER"));
	        	
	            return new TransferSuccessMessage(withdraw.getMessage(), deposit.getMessage(), withdraw.getBalance());
	        } else {
	            throw new InvalidAccessException();
	        }
	    }
	    throw new InsufficientBalanceException();
	}

	@Override
	public TransferSuccessMessage deductLockerCharges(DeductLockerChargesRequest deductLockerChargesRequest ) throws InvalidAccessException, InsufficientBalanceException{
    Account fromAccount = accountClient.getAccounts(deductLockerChargesRequest.getFromAccountId());
    Account toAccount = accountClient.getAccountBytype("BANK_ACCOUNT");
    double amount = deductLockerChargesRequest.getRent();
    if (isTransactionValid(fromAccount.getAccountType(), fromAccount.getBalance() - amount)) {
        String id = "TRANSACT" + repository.count();
        String narration = "Deposit locker charges";
        TransactionStatus withdraw = accountClient.withdraw(
                new TransactionDTO(deductLockerChargesRequest.getFromAccountId(), narration, amount, id));
        TransactionStatus deposit = accountClient.deposit(
                new TransactionDTO(toAccount.getAccountId(), narration, amount, id));

        if (withdraw != null && deposit != null) {
        repository.save(new TransactionHistory(id, deductLockerChargesRequest.getFromAccountId(), amount,
                narration, "Direct", toAccount.getAccountId(), new Date(),
                "LOCKER_CHARGE"));
        	
            return new TransferSuccessMessage(withdraw.getMessage(), deposit.getMessage(), withdraw.getBalance());
        } else {
            throw new InvalidAccessException("Something went wrong");
        }
    } else {
        throw new InsufficientBalanceException("Account has not much balance");
    }
}
	
	
	private static final Logger logger = Logger.getLogger(TransactionServiceImpl.class.getName());

	@Override
	public GiftTransactionStatus deductGiftCardCharges(DeductGiftCardCharges deductGiftCardCharges)
	        throws InvalidAccessException, InsufficientBalanceException {
	    try {
	    	String accountId=deductGiftCardCharges.getFromAccountId();
	    	accountClient.getAccounts(accountId);
	        Account fromAccount =accountClient.getAccounts(accountId);
//	        Account toAccount = accountClient.getAccountBytype("BANK_ACCOUNT");
	        double amount = deductGiftCardCharges.getAmount();

	        if (isTransactionValid(fromAccount.getAccountType(), fromAccount.getBalance() - amount)) {
	            String id = "TRANSACT" + repository.count();
	            String narrationForBank = "Deposit GIFT CARD charges";
	            String narrationForAccount = "Debit Gift Card Balance";
	            TransactionStatus withdraw = accountClient.withdraw(
	                    new TransactionDTO(fromAccount.getAccountId(), narrationForAccount, amount, id));
//	            TransactionStatus deposit = accountClient.deposit(
//	                    new TransactionDTO(toAccount.getAccountId(), narrationForBank, amount, id));

	            if (withdraw != null ) {
	            repository.save(new TransactionHistory(id, fromAccount.getAccountId(), amount,
	                    narrationForAccount, "Direct","GiftCardDeduction", new Date(),
	                    "GIFT_CARD_CHARGE"));

	                return new GiftTransactionStatus(withdraw.getMessage(), withdraw.getBalance());
	            } else {
	                throw new InvalidAccessException();
	            }
	        } else {
	            throw new InsufficientBalanceException();
	        }
	    } catch (InvalidAccessException e) {
	        logger.severe("Error deducting gift card charges: InvalidAccessException - " + e.getMessage());
	        throw e;
	    } catch (InsufficientBalanceException e) {
	        logger.severe("Error deducting gift card charges: InsufficientBalanceException - " + e.getMessage());
	        throw e;
	    } catch (Exception e) {
	        logger.severe("Error deducting gift card charges: " + e.getMessage());
	        throw e;
	    }
	}

	public GiftTransactionStatus availGiftCard(AvailGiftCard availGiftCard) throws InvalidAccessException
	{ 
		try {
	    	String accountId=availGiftCard.getToAccountId();
	    	accountClient.getAccounts(accountId);
	        Account toAccount =accountClient.getAccounts(accountId);
	        double amount = availGiftCard.getAmount();
	            String id = "TRANSACT" + repository.count();
	            String narrationForBank = "Availing GIFT CARD";
	            String narrationForAccount = "Adding Gift Card Balance";
	            TransactionStatus deposit = accountClient.deposit(
	                    new TransactionDTO(toAccount.getAccountId(), narrationForAccount, amount, id));
//	            TransactionStatus deposit = accountClient.deposit(
//	                    new TransactionDTO(toAccount.getAccountId(), narrationForBank, amount, id));

	            if (deposit != null ) {
	            repository.save(new TransactionHistory(id, toAccount.getAccountId(), amount,
	                    narrationForAccount, "Direct", toAccount.getAccountId(), new Date(),
	                    "GIFT_CARD_CHARGE"));

	                return new GiftTransactionStatus(deposit.getMessage(), deposit.getBalance());
	            } else {
	                throw new InvalidAccessException();
	            }
	    } catch (InvalidAccessException e) {
	        logger.severe("Error Availing gift card: InvalidAccessException - " + e.getMessage());
	        throw e;
	    }
	}


	@Override
	public TransferSuccessMessage deductCreditCardCharges(DeductCreditCardChargesRequest deductCreditCardChargesRequest ) throws InvalidAccessException, InsufficientBalanceException{
    Account fromAccount = accountClient.getAccounts(deductCreditCardChargesRequest.getFromAccountId());
    Account toAccount = accountClient.getAccountBytype("BANK_ACCOUNT");
    double amount = deductCreditCardChargesRequest.getAmount();
    if (isTransactionValid(fromAccount.getAccountType(), fromAccount.getBalance() - amount)) {
        String id = "TRANSACT" + repository.count();
        String narration = "Credit Card charges";
        TransactionStatus withdraw = accountClient.withdraw(
                new TransactionDTO(deductCreditCardChargesRequest.getFromAccountId(), narration, amount, id));
        TransactionStatus deposit = accountClient.deposit(
                new TransactionDTO(toAccount.getAccountId(), narration, amount, id));

        if (withdraw != null && deposit != null) {
        repository.save(new TransactionHistory(id, deductCreditCardChargesRequest.getFromAccountId(), amount,
                narration, "Direct", toAccount.getAccountId(), new Date(),
                "CREDIT-CARD_CHARGE"));
        	
            return new TransferSuccessMessage(withdraw.getMessage(), deposit.getMessage(), withdraw.getBalance());
        } else {
            throw new InvalidAccessException();
        }
    } else {
        throw new InsufficientBalanceException();
    }
}
	
	
	
	@Override
	public List<TransactionHistory> transactionHistory(String accountId)
	        throws InvalidAccessException, InvalidAccountIdException {
	    List<TransactionHistory> list = repository.findByAccountId(accountId);
	    if (list.isEmpty()) {
	        throw new InvalidAccountIdException();
	    }
	    return list;
	}


}