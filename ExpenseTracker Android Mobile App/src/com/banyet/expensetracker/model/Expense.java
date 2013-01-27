package com.banyet.expensetracker.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import android.location.Location;

public class Expense implements Serializable {
	private static final long serialVersionUID = 5624842684441827237L;
	private long id;
	private String name;
	private double amount;
	private Date date;
	private String note;
	private Location location;
	private String category;
	private List<sharedBillFriends> sharedEntities;
	// TODO: Add people

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Expense() {
	}

	public String getName() {
		if (name == null || name.isEmpty()) {
			return "Untitled transaction";
		}

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

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public List<sharedBillFriends> getSharedEntities() {
		return sharedEntities;
	}
}
