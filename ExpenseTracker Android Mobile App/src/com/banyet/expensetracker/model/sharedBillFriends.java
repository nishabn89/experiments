package com.banyet.expensetracker.model;

import java.io.Serializable;

public class sharedBillFriends implements Serializable {

	private static final long serialVersionUID = -7646426367028933753L;
	private String name;
	private double amount;

	public sharedBillFriends(String textName, double textAmt) {
		this.name = textName;
		this.amount = textAmt;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}
}
