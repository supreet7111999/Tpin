package com.axis.service;


import com.axis.exception.InsufficientBalanceException;
import com.axis.exception.InvalidAccessException;
import com.axis.model.Loan;

public interface LoanService {
	
	String applyLoan(Loan loan);
	String verifyLoan(Long id);//Bank authorize person take care of this
	String approveLoan(Long id);
	String disburseMoney(Long id ,  Loan loan) throws InvalidAccessException;
	String completedLoanInstallment(Long id);
	
	String rejectLoan(Long id);
	
	int calculateEmi(Loan loan);//User can calculate this
	double pendingAmount(Long id);
	Loan ChangeTenure(Long id ,int months); // user can request
	String payMonthlyEmi(Long id , double amount) throws InvalidAccessException, InsufficientBalanceException;
	int pendingEmi(Long id);
	double overDue(Long id);
	
}
