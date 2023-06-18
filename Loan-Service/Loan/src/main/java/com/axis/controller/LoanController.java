package com.axis.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.axis.model.Loan;
import com.axis.pdfGenerator.PDFGenerator;
import com.axis.service.LoanServiceImpl;
import com.lowagie.text.DocumentException;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/loan")
public class LoanController {
	
	private  LoanServiceImpl service;
	
	@PostMapping("/apply")
	public ResponseEntity<String>applyLoan(@RequestBody Loan loan ){
		
		String status = service.applyLoan(loan);
		
		return new ResponseEntity<String>(status , HttpStatus.CREATED);
	}
	
	@PutMapping("/verify/{id}")
	public ResponseEntity<String> verifyLoan( @PathVariable ("id") Long id ,@RequestBody Loan loan){
		
		String status = service.verifyLoan(id);
		
		return new ResponseEntity<String>(status ,HttpStatus.OK);
		
	}
	
	@PutMapping("/approve/{id}")
	public ResponseEntity<String> approveLoan( @PathVariable ("id") Long id ,@RequestBody Loan loan){
		
		String status = service.approveLoan(id);
		
		return new ResponseEntity<String>(status ,HttpStatus.OK);
		
	}
	@PutMapping("/disburse/{id}")
	public ResponseEntity<String> disburseMoney( @PathVariable ("id") Long id , @RequestBody Loan loan){
		
		String status = service.disburseMoney(id,loan);
		
		return new ResponseEntity<String>(status ,HttpStatus.OK);
		
	}
	@GetMapping ("/pendingAmount/{id}")
	public ResponseEntity<String>pendingAmount(@PathVariable ("id") Long id){
		double pendingAmount = service.pendingAmount(id);
		return new ResponseEntity<String>("Your pending amount is " + pendingAmount , HttpStatus.OK);
	}
	
	@PutMapping("/updateMonths/{id}")
	public ResponseEntity<String>updateTenure(@PathVariable ("id") Long id , int months){
		Loan loan = service.ChangeTenure(id, months);
		return new ResponseEntity<String>("Your loan tenure has been updated to " + loan.getTermInMonths() + " and emi has been updated to "+ loan.getEmi() , HttpStatus.OK);
	}
	
	@PutMapping("/payEmi/{id}")
	public ResponseEntity<String>payMonthlyEmi(@PathVariable ("id") Long id , double amount){
		String message = service.payMonthlyEmi(id, amount);
		return new ResponseEntity<String>(message, HttpStatus.ACCEPTED);
		
		
	}
	
	@PutMapping("/reject/{id}")
	public ResponseEntity<String> rejectLoan(@PathVariable ("id") Long id){
		
		String message = service.rejectLoan(id);
		
		return new ResponseEntity<String>(message,HttpStatus.ACCEPTED);
	}
	
	@GetMapping("/pendingEMI/{id}")
	public ResponseEntity<String> pendingEMI(@PathVariable ("id") Long id){
		
		int numberOfMonths = service.pendingEmi(id);
		
		return new ResponseEntity<String>("Still You have to pay " +  numberOfMonths + " dues." , HttpStatus.OK);
		
	}
	@GetMapping("/getLoanById/{id}")
	public ResponseEntity<Loan> getLoanById(@PathVariable ("id") Long id){
		
		return new ResponseEntity<Loan>(service.getLoanByID(id), HttpStatus.OK);
	}
	@PutMapping("/emiCalculator")
	public ResponseEntity<String> calculateEMI(@RequestBody Loan loan){
		int emi = service.calculateEmi(loan);
		
		return new ResponseEntity<String>("Your montly EMI will be calculated based on data that you have provided " + emi , HttpStatus.OK);
	}
	 @GetMapping("/pdf/loans")
		public void generatePdf(HttpServletResponse response) throws DocumentException, IOException, java.io.IOException {
			
			response.setContentType("application/pdf");
			DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-DD:HH:MM:SS");
			String currentDateTime = dateFormat.format(new Date(00, 00, 00));
			String headerkey = "Content-Disposition";
			String headervalue = "attachment; filename=pdf_Loan_List" + currentDateTime + ".pdf";
			response.setHeader(headerkey, headervalue);
			
			List<Loan> loanList = service.getAllLoans();
			
			PDFGenerator generator = new PDFGenerator();
			 generator.setLoanList(loanList);
			generator.generate(response);

}
}
