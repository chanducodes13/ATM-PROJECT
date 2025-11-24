package com.codegnan.interfaces;

import com.codegnan.Exceptions.InsufficientBalanceException;
import com.codegnan.Exceptions.InsufficientMachineBalanceException;
import com.codegnan.Exceptions.InvalidAmountException;
import com.codegnan.Exceptions.NotAOperatorException;

public interface IATMService {
	// to get the user type whether the user is operator or normal
	public abstract String getUserType() throws NotAOperatorException;
	// to withdraw an amount
	// throws exceptions for invalid inputs

	public abstract double withdrawAmount(double withAmount)
			throws InvalidAmountException, InsufficientBalanceException, InsufficientMachineBalanceException;

	// to deposit amount
	public abstract void depositAmount(double dptAmount) throws InvalidAmountException;

	public abstract double checkAccountBalance();

	public abstract void changePinNUmber(int pinNumber);

	public abstract int getPinNumber();

	public abstract String getUserName();

	public abstract void decreaseChances();

	public abstract int getChances();

	public abstract void resetPinChances();

	public abstract void generateMiniStatements();

}
