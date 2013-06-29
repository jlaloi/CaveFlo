package com.caveflo.cave;

public class UserSavedData {

	private String id;
	private String drunk;
	private int rating;

	public UserSavedData(String id, String drunk, int rating) {
		super();
		this.id = id;
		this.drunk = drunk;
		this.rating = rating;
	}

	public UserSavedData(Biere biere) {
		super();
		this.id = biere.getId();
		this.drunk = biere.getDrunk();
		this.rating = biere.getRating();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDrunk() {
		return drunk;
	}

	public void setDrunk(String drunk) {
		this.drunk = drunk;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

}
