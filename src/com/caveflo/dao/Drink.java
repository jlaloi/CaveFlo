package com.caveflo.dao;

import java.io.Serializable;
import java.util.Calendar;

public class Drink implements Serializable {

	private static final long serialVersionUID = 1253315814460248638L;

	private int id;
	private String note;
	private float degree;
	private int quantity;
	private int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public float getDegree() {
		return degree;
	}

	public void setDegree(float degree) {
		this.degree = degree;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getHour() {
		return hour;
	}

	public void setHour(int hour) {
		this.hour = hour;
	}

	public String toString() {
		return hour + " " + note + " (" + id + ") " + quantity + " at " + degree;
	}

}
