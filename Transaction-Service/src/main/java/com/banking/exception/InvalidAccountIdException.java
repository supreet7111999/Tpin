package com.banking.exception;

public class InvalidAccountIdException extends Exception {

	private static final long serialVersionUID = -6016848453903470899L;
    public InvalidAccountIdException() {
        super();
    }

    public InvalidAccountIdException(String message) {
        super(message);
    }
}