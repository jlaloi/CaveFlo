package com.caveflo.dao;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import com.caveflo.misc.Factory;
import com.caveflo.misc.Tools;

public class BeerReferential {

	public static final String referentialUrl = "https://github.com/jlaloi/CaveFlo/raw/master/caveflodb.csv";
	public static final String referentialSep = ";";
	private List<Beer> beers = new ArrayList<Beer>();
	private BeerDataSource beerDataSource;

	public BeerReferential() {
		beers = new ArrayList<Beer>();
		beerDataSource = Factory.get().getBeerDataSource();
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

	private void updateFromLines(List<String> lines) {
		for (String line : lines) {
			String[] split = line.split(referentialSep);
			if (split.length > 6) {
				String id = split[0];
				String name = split[1];
				Float degree = Float.valueOf(split[2]);
				String type = split[3];
				String country = split[4];
				int status = Integer.valueOf(split[5]);
				int custom = Integer.valueOf(split[6]);
				if (beerDataSource.updateBeer(id, name, degree, type, country, status, custom) == -1) {
					beerDataSource.createBeer(id, name, degree, type, country, status, custom);
				}
			}
		}
		load();
	}

	public List<Beer> load() {
		beers.clear();
		beers = beerDataSource.getAllBeer();
		Collections.sort(beers, Factory.get().getBeerComparator());
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
	
	private void add(Beer beer){
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

	public void saveRating(Beer beer){
		if(beerDataSource.updateRating(beer) == -1){
			beerDataSource.createRating(beer);
		}
	}
	public void deleteRating(Beer beer) {
		beerDataSource.deleteRating(beer);
	}

}
