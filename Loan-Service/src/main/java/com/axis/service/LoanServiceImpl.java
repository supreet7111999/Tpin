package com.axis.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.axis.clients.AccountClient;
import com.axis.clients.TransactionClient;
import com.axis.dto.AccountDTO;
import com.axis.exception.InsufficientBalanceException;
import com.axis.exception.InvalidAccessException;
import com.axis.model.EmploymentStatus;
import com.axis.model.Loan;
import com.axis.model.LoanStatus;
import com.axis.model.LoanType;
import com.axis.repository.LoanRepository;
import com.axis.dto.*;
import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class LoanServiceImpl implements LoanService{
	
	@Autowired
	private final LoanRepository repo;
	@Autowired
	private TransactionClient transactionClient;
	@Autowired
	private AccountClient accountClient;

	@Override
	public String applyLoan(Loan loan) {
		// TODO Auto-generated method stub
		if(loan.getAmount() > 3000000 && loan.getAmount()<50000 ) {
			if(loan.getEmpStatus()  == EmploymentStatus.UNEMPLOYEE ) {
				if(loan.getCibilScore()<700) {
					loan.setStatus(LoanStatus.REJECTED);
					loan.setCreatedAt(LocalDateTime.now());
					loan.setUpdatedAt(LocalDateTime.now());
					repo.save(loan);
					return " Sorry we cannot process loan for you";
				}
			}
		}
		AccountDTO account=accountClient.getCustomerAccounts(loan.getUserId());
		if(account==null)
		{
			return "Open account in Bank";
		}
		loan.setLoanType(loan.getLoanType());
		String prefix="";
		if(loan.getLoanType() == LoanType.STUDENT) {
			prefix ="EL";
		}
		else if(loan.getLoanType() == LoanType.PERSONAL) {
			prefix ="PL";
		}
		else {
			prefix = "HL";
		}
		
		String loanNumber = prefix + generateUniqueLoanNumber();
		loan.setLoanNumber(loanNumber);
		loan.setAmount(loan.getAmount());
		loan.setInterestRate(0);
		loan.setTermInMonths(0);
		loan.setStatus(LoanStatus.PENDING);
		loan.setEmpStatus(loan.getEmpStatus());
		loan.setIncome(loan.getIncome());
		loan.setPancardNumber(loan.getPancardNumber());
		loan.setCibilScore(loan.getCibilScore());
		loan.setEmi(0);
		loan.setCreatedAt(LocalDateTime.now());
		loan.setUpdatedAt(LocalDateTime.now());
		loan.setUserId(loan.getUserId());
		loan.setAccountId(loan.getAccountId());
		repo.save(loan);
		return "Loan Applied Successfully";
	}
	 private String generateUniqueLoanNumber() {
	        ThreadLocalRandom random = ThreadLocalRandom.current();
	        int loanNumber;
	       
	            loanNumber = random.nextInt(1_000_00_000);
	           
	        return String.format("%08d", loanNumber);
	    }

	//Physically verified
	@Override
	public String verifyLoan(Long id) {
		// TODO Auto-generated method stub
		Loan loan = repo.findById(id).get();
		loan.setStatus(LoanStatus.ONPROCESS);
		loan.setUpdatedAt(LocalDateTime.now());
		 repo.save(loan);
		 return "Verification Completed";
	}

	@Override
	public String approveLoan(Long id) {
		// TODO Auto-generated method stub
		Loan loan = repo.findById(id).get();
		loan.setUpdatedAt(LocalDateTime.now());
		loan.setStatus(LoanStatus.APPROVED);
		repo.save(loan);
		return "Loan has been approved";
	}

	@Override
	public String disburseMoney(Long id, Loan disbursedloan) throws InvalidAccessException {
		// TODO Auto-generated method stub
		  Loan loan = repo.findById(id).get();
		    loan.setAmount(disbursedloan.getAmount());
		    loan.setInterestRate(disbursedloan.getInterestRate());
		    loan.setTermInMonths(disbursedloan.getTermInMonths());
		    loan.setStatus(LoanStatus.ACTIVE);
		    loan.setEmi(calculateEmi(disbursedloan));
		    loan.setUpdatedAt(LocalDateTime.now());
		    loan.setPaidMonths(0);
		    loan.setRemainingMonths(disbursedloan.getTermInMonths());
		    // Update the existing loan row in the data table
		    
		    //Add amount in the Account
		    String narration="Loan Amount is added";
		    transactionClient.deposit(new OneWayTransactionDTO(loan.getAccountId(),narration,"LOAN_TRANSACTION",disbursedloan.getAmount()));
		    repo.save(loan);     
		return "Loan amount has been disbursed";
	}
	
    //Loan completion
	@Override
	public String completedLoanInstallment(Long id ) {
		// TODO Auto-generated method stub
		Loan  loan = repo.findById(id).get();

		if( loan.getTermInMonths() == 0) {
			loan.setUpdatedAt(LocalDateTime.now());

			loan.setStatus(LoanStatus.CLOSED);
		}
		repo.save(loan);
		return "You have sucessfully paid , all your loan money ! Feel free to apply loan again 😊😊";
	}

	
	@Override
	public int calculateEmi(Loan loan) {
//		int loanTenureMonths, double loanAmount , double annualInterestRate 
		int loanTenureMonths = loan.getTermInMonths(); 
		double loanAmount = loan.getAmount();
		double annualInterestRate = loan.getInterestRate();
		double monthlyInterestRate = (annualInterestRate / 100) / 12;
        double denominator = Math.pow(1 + monthlyInterestRate, loanTenureMonths) - 1;
        int emi = (int) (((loanAmount * monthlyInterestRate * Math.pow(1 + monthlyInterestRate, loanTenureMonths))) / denominator);
        return  emi;
	}
    
	@Override
	public double pendingAmount(Long id) {
		// TODO Auto-generated method stub
		Loan loan = repo.findById(id).get();
		double emi = calculateEmi(loan);
		double pendingAmount = emi * loan.getRemainingMonths();
		return pendingAmount;
	}

//	---------------------------------------------------------------------
	
	@Override
	public Loan ChangeTenure(Long id , int months) {
		// TODO Auto-generated method stub	
		Loan loan = repo.findById(id).get();
		loan.setTermInMonths(months);
		loan.setEmi(calculateEmi(loan));
		loan.setUpdatedAt(LocalDateTime.now());
		repo.save(loan);
		return loan;
	}

	
	
	@Override
	public String payMonthlyEmi(Long id , double amount) throws InvalidAccessException, InsufficientBalanceException {
		// TODO Auto-generated method stub
		Loan loan = repo.findById(id).get();
		
		if(amount == loan.getEmi()) {
			loan.setPaidMonths(loan.getPaidMonths() +1 );
			loan.setRemainingMonths(loan.getRemainingMonths() -1 );
//			 over due date
//			LocalDateTime lastduedate =  loan.getUpdatedAt() ;
//			LocalDateTime currentDate = lastduedate.plusMonths(1).plusDays(1);
//			ChronoUnit - it is used to calculate the difference between last month date and previous month date
//	        long daysDifference = ChronoUnit.DAYS.between(lastduedate, currentDate);

			loan.setUpdatedAt(LocalDateTime.now());
			//Add logic for deducting money using transaction feign client
			transactionClient.withdraw(new OneWayTransactionDTO(loan.getAccountId(),"Deducted EMI","LOAN_EMI",loan.getEmi()));
			repo.save(loan);
     
			if(loan.getRemainingMonths() == 0)
			{
				return completedLoanInstallment(id);
			}
			return " You have paid ur monthly emi";
		}
		else
		{
			return "You are not paying appropriate amount";
		}
		
	}

	@Override
	public String rejectLoan(Long id) {
		// TODO Auto-generated method stub
		Loan loan = repo.findById(id).get();
		loan.setStatus(LoanStatus.REJECTED);
		repo.save(loan);
		return "Sorry , We cannot process the loan for you ";
	}

	@Override
	public int pendingEmi(Long id) {
		Loan loan = repo.findById(id).get();
		return loan.getRemainingMonths();
	}

	
	//Need to check once again
	@Override
	public double overDue(Long id) {
		Loan loan = repo.findById(id).get();
		
		double emi = loan.getEmi();
		 
		emi = emi + (emi * 1.5/100);
		
		return emi;
		
	}

	public Loan getLoanByID(Long id) {
		// TODO Auto-generated method stub
		return repo.findById(id).get();
	}
	public List<Loan> getAllLoans() {
		// TODO Auto-generated method stub
		return repo.findAll();
	}
	
}
