package com.caveflo;

import java.util.Comparator;

public class Biere implements Comparator<Biere> {

	private String id;
	private String name;
	private float degree;
	private String status;
	private String drunk = "";
	
	public Biere(){
		super();
	}

	public Biere(String id, String name, float degree, String status,
			String drunk) {
		super();
		this.id = id;
		this.name = name;
		this.degree = degree;
		this.status = status;
		this.drunk = drunk;
	}

	public Biere(String id, String name, float degree, String status) {
		super();
		this.id = id;
		this.name = name;
		this.degree = degree;
		this.status = status;
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


	public int compare(Biere lhs, Biere rhs) {
		return lhs.getName().compareTo(rhs.getName());
	}

	
}
