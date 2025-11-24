package com.codegnan.cards;

import com.codegnan.Exceptions.InsufficientBalanceException;
import com.codegnan.Exceptions.InsufficientMachineBalanceException;
import com.codegnan.Exceptions.InvalidAmountException;
import com.codegnan.Exceptions.NotAOperatorException;
import com.codegnan.interfaces.IATMService;

public class OpertorCard implements IATMService {
	private long id;
	private int pinNumber;
	private String name;
	private final String type = "user";

	public OpertorCard(long id, int pinNumber, String name) {
		super();
		this.id = id;
		this.pinNumber = pinNumber;
		this.name = name;
	}

	@Override
	public String getUserType() throws NotAOperatorException {
		return type;
	}

	@Override
	public double withdrawAmount(double withAmount)
			throws InvalidAmountException, InsufficientBalanceException, InsufficientMachineBalanceException {
		return 0;
	}

	@Override
	public void depositAmount(double dptAmount) throws InvalidAmountException {

	}

	@Override
	public double checkAccountBalance() {
		return 0;
	}

	@Override
	public void changePinNUmber(int pinNumber) {

	}

	@Override
	public int getPinNumber() {

		return pinNumber;
	}

	@Override
	public String getUserName() {

		return name;
	}

	@Override
	public void decreaseChances() {

	}

	@Override
	public int getChances() {

		return 0;
	}

	@Override
	public void resetPinChances() {

	}

	@Override
	public void generateMiniStatements() {

	}

}
