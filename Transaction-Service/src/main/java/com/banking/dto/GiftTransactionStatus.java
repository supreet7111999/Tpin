package com.banking.dto;

public class GiftTransactionStatus {
	private String fromAccount;
	private double sourceBalance;
	public GiftTransactionStatus() {
		super();
		// TODO Auto-generated constructor stub
	}
	public GiftTransactionStatus(String fromAccount, double sourceBalance) {
		super();
		this.fromAccount = fromAccount;
		this.sourceBalance = sourceBalance;
	}
	public String getFromAccount() {
		return fromAccount;
	}
	public void setFromAccount(String fromAccount) {
		this.fromAccount = fromAccount;
	}
	public double getSourceBalance() {
		return sourceBalance;
	}
	public void setSourceBalance(double sourceBalance) {
		this.sourceBalance = sourceBalance;
	}
	
}
