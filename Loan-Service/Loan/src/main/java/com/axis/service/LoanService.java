package com.axis.service;


import com.axis.model.Loan;

public interface LoanService {
	
	String applyLoan(Loan loan);
	String verifyLoan(Long id);//Bank authorize person take care of this
	String approveLoan(Long id);
	String disburseMoney(Long id ,  Loan loan);
	String completedLoanInstallment(Long id);
	
	String rejectLoan(Long id);
	
	int calculateEmi(Loan loan);//User can calculate this
	double pendingAmount(Long id);
	Loan ChangeTenure(Long id ,int months); // user can request
	String payMonthlyEmi(Long id , double amount);
	int pendingEmi(Long id);
	double overDue(Long id);
	
}
