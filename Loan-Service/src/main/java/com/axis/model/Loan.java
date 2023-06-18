package com.axis.model;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor

@Entity
@Table(name = "Loan_Details")
public class Loan {
	
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    @Enumerated(EnumType.STRING)
	    private LoanType loanType;
	    private String loanNumber;
	    private double amount;
	    private double interestRate;
	    private int termInMonths;

	    @Enumerated(EnumType.STRING)
	    private LoanStatus status;
	    
	    @Enumerated(EnumType.STRING)
	    private EmploymentStatus empStatus;
	    
	    private double income;
	    private String pancardNumber;
	    
	    
	    
	    private Integer cibilScore;
	    
	    private double emi;
	    private int paidMonths;
	    private int remainingMonths;
	    private LocalDateTime createdAt;
	    private LocalDateTime updatedAt;
	    //add userId , AccpontId
	    private long userId;
	    private String accountId;
	    private static final DateTimeFormatter DATE_TIME_FORMATTER =
	            DateTimeFormatter.ofPattern("d/M/yyyy HH:mm:ss");

        //adding these two fields in constructor
		public Loan(Long id, LoanType loanType, double amount, double interestRate, int termInMonths, LoanStatus status,
				EmploymentStatus empStatus, double income, Integer creditScore, double emi, LocalDateTime createdAt,
				LocalDateTime updatedAt,   String pancardNumber ,   int paidMonths,int remainingMonths,String loanNumber,long userId,String accountId) {
			super();
			this.userId=userId;
			this.accountId=accountId;
			this.id = id;
			this.loanType = loanType;
			this.amount = amount;
			this.interestRate = interestRate;
			this.termInMonths = termInMonths;
			this.status = status;
			this.empStatus = empStatus;
			this.income = income;
			this.cibilScore = creditScore;
			this.emi = emi;
			 this.createdAt = LocalDateTime.parse(
		                DATE_TIME_FORMATTER.format(LocalDateTime.now()),
		                DATE_TIME_FORMATTER);
		        this.updatedAt = createdAt;
		        this.pancardNumber = pancardNumber;
		        this.paidMonths=paidMonths;
		        this.remainingMonths = remainingMonths;
		        this.loanNumber = loanNumber;
		}
	    
	    
	    

}
