package com.codegnan.Exceptions;

public class InsufficientBalanceException extends Exception{
	public InsufficientBalanceException(String errorMsg) {
		super(errorMsg);
	}

}
