package com.sr.dto;

public class AvailGiftCard {
    public String toAccountId;
    public double amount;
	public AvailGiftCard(String toAccountId, double amount) {
		super();
		this.toAccountId = toAccountId;
		this.amount = amount;
	}
	public AvailGiftCard() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getToAccountId() {
		return toAccountId;
	}
	public void setToAccountId(String toAccountId) {
		this.toAccountId = toAccountId;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
    
}
