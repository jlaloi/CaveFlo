package com.caveflo.dao;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import android.util.Log;

import com.caveflo.misc.Factory;
import com.caveflo.misc.Tools;

public class BeerReferential {

	public static final String referentialUrl = "https://github.com/jlaloi/CaveFlo/raw/master/Referential.csv";
	public static final String referentialSep = ";";
	private List<Beer> beers = new ArrayList<Beer>();
	private BeerDataSource beerDataSource;

	public BeerReferential(BeerDataSource beerDataSource) {
		beers = new ArrayList<Beer>();
		this.beerDataSource = beerDataSource;
	}

	public boolean update() {
		List<String> lines = Tools.downloadFile(referentialUrl);
		updateFromLines(lines);
		return lines.size() > 0;
	}

	public void update(File file) {
		List<String> lines = Tools.read(file);
		updateFromLines(lines);
	}

	public void updateRating(File file) {
		List<String> lines = Tools.read(file);
		updateRatingFromLines(lines);
	}

	private void updateFromLines(List<String> lines) {
		for (String line : lines) {
			try {
				String[] split = line.split(referentialSep);
				String type = "";
				String country = "";
				int status = 1;
				int custom = 1;
				Float degree = 0f;
				if (split.length > 1) {
					String id = split[0];
					String name = split[1];
					if (split.length > 2) {
						if (split[2].trim().length() > 0) {
							degree = Float.valueOf(split[2]);
						}
						if (split.length > 3) {
							type = split[3];
							if (split.length > 4) {
								country = split[4];
								if (split.length > 5) {
									status = Integer.valueOf(split[5]);
									if (split.length > 6) {
										if (split[6].trim().length() > 0) {
											custom = Integer.valueOf(split[6]);
										}
									}
								}
							}
						}
					}
					if (beerDataSource.updateBeer(id, name, degree, type, country, status, custom) == 0) {
						beerDataSource.createBeer(id, name, degree, type, country, status, custom);
					}
					Log.i("Load beer", "Beer created/updated, ID: " + id + ", name: " + name);
				}
			} catch (Exception e) {
				Log.e("LINE REFRESH", "Error while parsing " + line, e);
				e.printStackTrace();
			}
		}
		load();
	}

	public int saveCustomBeerToFile(File file) {
		List<String> lines = new ArrayList<String>();
		for (Beer beer : beers) {
			if (beer.isCustom()) {
				lines.add(beerToLine(beer));
			}
		}
		Tools.write(file, lines);
		return lines.size();
	}

	private String beerToLine(Beer beer) {
		String result = "";
		result += beer.getId() + referentialSep;
		result += beer.getName() + referentialSep;
		result += beer.getDegree() + referentialSep;
		result += beer.getType() + referentialSep;
		result += beer.getCountry() + referentialSep;
		result += beer.getStatus() + referentialSep;
		result += beer.getCustom() + referentialSep;
		return result;
	}

	private void updateRatingFromLines(List<String> lines) {
		for (String line : lines) {
			try {
				String[] split = line.split(referentialSep);
				if (split.length > 2) {
					String id = split[0];
					Beer beer = getBeer(id);
					if (beer != null) {
						beer.setRatingDate(split[1]);
						beer.setRating(Integer.valueOf(split[2]));
						if (split.length > 3) {
							beer.setComment(split[3]);
						} else {
							beer.setComment("");
						}
						if (beerDataSource.updateRating(beer) == 0) {
							beerDataSource.createRating(beer);
						}
						Log.i("Load rating", "Beer rating updated, ID: " + id);
					} else {
						Log.i("Load rating", "Beer not found, ID: " + id);
					}
				}
			} catch (Exception e) {
				Log.e("Load rating from file", "Error while parsing " + line, e);
			}
		}
		load();
	}

	private String ratingToLine(Beer beer) {
		String result = "";
		result += beer.getId() + referentialSep;
		result += beer.getRatingDate() + referentialSep;
		result += beer.getRating() + referentialSep;
		result += beer.getComment();
		return result;
	}

	public int saveRatingToFile(File file) {
		List<String> lines = new ArrayList<String>();
		for (Beer beer : beers) {
			if (beer.isDrunk()) {
				lines.add(ratingToLine(beer));
			}
		}
		Tools.write(file, lines);
		return lines.size();
	}

	public Beer getBeer(String id) {
		Beer result = null;
		for (Beer beer : beers) {
			if (beer.getId().equals(id)) {
				result = beer;
				break;
			}
		}
		return result;
	}

	public List<Beer> load() {
		beers.clear();
		beers = beerDataSource.getAllBeerWithRating();
		Collections.sort(beers, Factory.get().getBeerComparator());
		Log.i("BEER DB", "Count: " + beers.size());
		return beers;
	}

	public List<Beer> getBeers() {
		return beers;
	}

	public Beer createBeer(String id, String name, float degree, String type, String country, int status, int custom) {
		Beer beer = beerDataSource.createBeer(id, name, degree, type, country, status, custom);
		add(beer);
		return beer;
	}

	public Beer createCustomBeer(String name, float degree, String type, String country) {
		Beer beer = beerDataSource.createBeer("#" + Calendar.getInstance().getTimeInMillis(), name, degree, type, country, 1, 1);
		add(beer);
		return beer;
	}

	private void add(Beer beer) {
		beers.add(beer);
		Collections.sort(beers, Factory.get().getBeerComparator());
	}

	public Beer updateBeer(Beer beer) {
		beerDataSource.updateBeer(beer);
		return beer;
	}

	public void deleteBeer(Beer beer) {
		beerDataSource.deleteBeer(beer);
		beers.remove(beer);
	}

	public void saveRating(Beer beer) {
		if (beerDataSource.updateRating(beer) == 0) {
			beerDataSource.createRating(beer);
		}
	}

	public void deleteRating(Beer beer) {
		beerDataSource.deleteRating(beer);
	}

	public List<String> getBeerTypes() {
		return beerDataSource.getBeerTypes();
	}

	public List<String> getBeerCountries() {
		return beerDataSource.getBeerCountries();
	}

	public int getBeerCount() {
		return beers.size();
	}

	public int getBeerDrunkCount() {
		int result = 0;
		for (Beer beer : beers) {
			if (beer.isDrunk()) {
				result++;
			}
		}
		return result;
	}
}
