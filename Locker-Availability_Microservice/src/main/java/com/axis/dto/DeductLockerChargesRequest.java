package com.axis.dto;


public class DeductLockerChargesRequest {
    private String fromAccountId;
    private int rent;

    public DeductLockerChargesRequest() {
    }

    public DeductLockerChargesRequest(String fromAccountId, int rent) {
        this.fromAccountId = fromAccountId;
        this.rent = rent;
    }

    public String getFromAccountId() {
        return fromAccountId;
    }

    public void setFromAccountId(String fromAccountId) {
        this.fromAccountId = fromAccountId;
    }

    public int getRent() {
        return rent;
    }

    public void setRent(int rent) {
        this.rent = rent;
    }
}
