package com.banking.exception;

public class InsufficientBalanceException extends Exception {

	private static final long serialVersionUID = -2611352802405846959L;
    public InsufficientBalanceException() {
        super();
    }

    public InsufficientBalanceException(String message) {
        super(message);
    }
	

}