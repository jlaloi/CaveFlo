package com.caveflo.cave;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Biere implements Serializable {

	private static final long serialVersionUID = 110072984511839423L;
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy", Locale.FRENCH);

	private String id;
	private String name;
	private float degree;
	private String status;
	private String drunk = "";
	private int rating;
	private boolean custom = false;

	public Biere(String id, String name, float degree, String status, String drunk, int rating) {
		this.id = id;
		this.name = name;
		this.degree = degree;
		this.status = status;
		this.drunk = drunk;
		this.rating = rating;
	}

	public Biere(String id, String name, float degree, String status) {
		this.id = id;
		this.name = name;
		this.degree = degree;
		this.status = status;
	}

	public Biere(String id, String name, float degree) {
		this.id = id;
		this.name = name;
		this.degree = degree;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getDegree() {
		return degree;
	}

	public void setDegree(float degree) {
		this.degree = degree;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDrunk() {
		return drunk;
	}

	public void setDrunk(String drunk) {
		this.drunk = drunk;
	}

	public void setDrunk(Date date) {
		this.drunk = dateFormat.format(date);
	}

	public boolean isDrunk() {
		return drunk != null && drunk.trim().length() > 0;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public boolean isCustom() {
		return custom;
	}

	public void setCustom(boolean custom) {
		this.custom = custom;
	}

}
