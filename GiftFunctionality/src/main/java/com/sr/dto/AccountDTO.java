package com.sr.dto;

public class AccountDTO{

    private String accountId;
    private String accountType;
    private Long userId;
    private double balance;

    public AccountDTO() {
    	super();
    }

    public AccountDTO(String accountId, String accountType, Long userId, double balance) {
        this.accountId = accountId;
        this.accountType = accountType;
        this.userId = userId;
        this.balance = balance;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
