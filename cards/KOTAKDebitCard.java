package com.codegnan.cards;

import java.util.ArrayList;
import java.util.Collections;

import com.codegnan.Exceptions.InsufficientBalanceException;
import com.codegnan.Exceptions.InsufficientMachineBalanceException;
import com.codegnan.Exceptions.InvalidAmountException;
import com.codegnan.Exceptions.NotAOperatorException;
import com.codegnan.interfaces.IATMService;

public class KOTAKDebitCard implements IATMService {
	String name;
	long debitcardNumber;
	double accountBalnce;
	int pinNumber;
	ArrayList<String> statement;
	final String type = "user";
	int chances;

	public KOTAKDebitCard(String name, long debitcardNumber, double accountBalance, int pinNumber) {
		chances = 3;
		this.name = name;
		this.debitcardNumber = debitcardNumber;
		this.accountBalnce = accountBalance;
		this.pinNumber = pinNumber;
		statement = new ArrayList<>();
	}

	@Override
	public String getUserType() throws NotAOperatorException {

		return type;
	}

	@Override
	public double withdrawAmount(double withAmount)
			throws InvalidAmountException, InsufficientBalanceException, InsufficientMachineBalanceException {
		if (withAmount <= 0) {
			throw new InvalidAmountException("you can enter zero amount" + "to withdraw please enter valid amount");
		} else {
			if (withAmount % 100 != 0) {
				throw new InvalidAmountException("please with draw Multiples of humdreds");
			} else {
				accountBalnce -= withAmount;
				statement.add("Debited:" + withAmount);
				return withAmount;
			}
		}
	}

	@Override
	public void depositAmount(double dptAmount) throws InvalidAmountException {
		if (dptAmount <= 0 || dptAmount % 100 != 0) {
			throw new InvalidAmountException(
					"you cant deposite less than zero rupess" + "and deposie multiples of 100");
		} else {
			accountBalnce += dptAmount;
			statement.add("credited: " + dptAmount);
		}

	}

	@Override
	public double checkAccountBalance() {

		return accountBalnce;
	}

	@Override
	public void changePinNUmber(int pinNumber) {

		this.pinNumber = pinNumber;
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
		--chances;

	}

	@Override
	public int getChances() {

		return chances;
	}

	@Override
	public void resetPinChances() {
		chances = 3;

	}

	@Override
	public void generateMiniStatements() {
		int count = 5;
		if (statement.size() == 0) {
			System.out.println("There is no transactions occurred");
			return;
		}
		System.out.println("=================List of 5 Transactions============");
		for (String trans : statement) {
			if (count == 0) {
				break;
			}
			System.out.println(trans);
			count--;
		}
		Collections.reverse(statement);

	}

}
