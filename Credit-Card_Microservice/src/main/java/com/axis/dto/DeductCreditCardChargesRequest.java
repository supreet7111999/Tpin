package com.axis.dto;

public class DeductCreditCardChargesRequest {
	private String fromAccountId;
	private double amount;
	
	public String getFromAccountId() {
		return fromAccountId;
	}
	public void setFromAccountId(String fromAccountId) {
		this.fromAccountId = fromAccountId;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public DeductCreditCardChargesRequest(String fromAccountId, double amount) {
		super();
		this.fromAccountId = fromAccountId;
		this.amount = amount;
	}
	public DeductCreditCardChargesRequest() {
		super();
		// TODO Auto-generated constructor stub
	}
}
