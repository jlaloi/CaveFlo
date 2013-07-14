package com.caveflo.dao;

import java.io.Serializable;

public class Beer implements Serializable {

	private static final long serialVersionUID = 110072984511839423L;

	public static final int customValue = 1;
	public static final int validValue = 1;

	private String id = "";
	private String name = "";
	private float degree;
	private String type = "";
	private String country = "";
	private Integer status;
	private Integer custom;
	private Integer rating;
	private String comment = "";
	private String rating_date = "";

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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getCustom() {
		return custom;
	}

	public void setCustom(Integer custom) {
		this.custom = custom;
	}

	public Integer getRating() {
		return rating;
	}

	public void setRating(Integer rating) {
		this.rating = rating;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getRatingDate() {
		return rating_date;
	}

	public void setRatingDate(String date) {
		this.rating_date = date;
	}

	public boolean isDrunk() {
		return rating_date != null && rating_date.trim().length() > 0;
	}

	public boolean isCustom() {
		return custom == customValue;
	}

	public boolean isValid() {
		return status == validValue;
	}

	public String toString() {
		return name;
	}

}
