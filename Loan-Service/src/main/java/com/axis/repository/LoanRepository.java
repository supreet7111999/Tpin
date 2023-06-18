package com.axis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.axis.model.EmploymentStatus;
import com.axis.model.Loan;
import com.axis.model.LoanStatus;
import com.axis.model.LoanType;

import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {


    
}
