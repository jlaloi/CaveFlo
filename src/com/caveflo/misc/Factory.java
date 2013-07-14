package com.caveflo.misc;

import java.util.Comparator;
import java.util.Locale;

import android.app.Fragment;
import android.content.Context;
import android.util.DisplayMetrics;

import com.caveflo.dao.Beer;
import com.caveflo.dao.BeerDataSource;
import com.caveflo.dao.BeerReferential;
import com.caveflo.fragment.Info;
import com.caveflo.fragment.News;
import com.caveflo.fragment.bloodalchohol.BloodAlcoholContent;
import com.caveflo.fragment.cave.Cave;

public class Factory {

	private static final Factory instance = new Factory();

	public static Factory get() {
		return instance;
	}

	private Cave fragmentCave;
	private Fragment fragmentInfo;
	private Fragment fragmentNews;
	private BloodAlcoholContent fragmentAlcoolemie;
	private BeerDataSource beerDataSource;
	private BeerReferential beerReferential;
	private Comparator<Beer> beerComparator;
	private DisplayMetrics displayMetrics;

	public Factory() {
		fragmentCave = new Cave();
		fragmentInfo = new Info();
		fragmentNews = new News();
		fragmentAlcoolemie = new BloodAlcoholContent();
		beerComparator = new Comparator<Beer>() {
			public int compare(Beer lhs, Beer rhs) {
				return lhs.getName().toLowerCase(Locale.getDefault()).compareTo(rhs.getName().toLowerCase(Locale.getDefault()));
			}
		};
		displayMetrics = new DisplayMetrics();
	}

	public BeerDataSource initiateBeerDataSource(Context context) {
		beerDataSource = new BeerDataSource(context);
		beerDataSource.open();
		beerReferential = new BeerReferential(beerDataSource);
		beerReferential.load();
		if (beerReferential.getBeers().size() == 0) {
			new ASynchUpdate(context).execute();
		}
		return beerDataSource;
	}

	public static Factory getInstance() {
		return instance;
	}

	public Cave getFragmentCave() {
		return fragmentCave;
	}

	public Fragment getFragmentInfo() {
		return fragmentInfo;
	}

	public Fragment getFragmentNews() {
		return fragmentNews;
	}

	public BeerDataSource getBeerDataSource() {
		return beerDataSource;
	}

	public BeerReferential getBeerReferential() {
		return beerReferential;
	}

	public Comparator<Beer> getBeerComparator() {
		return beerComparator;
	}

	public BloodAlcoholContent getFragmentAlcoolemie() {
		return fragmentAlcoolemie;
	}

	public DisplayMetrics getDisplayMetrics() {
		return displayMetrics;
	}

	public void setDisplayMetrics(DisplayMetrics displayMetrics) {
		this.displayMetrics = displayMetrics;
	}

}
