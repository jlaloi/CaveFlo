package com.caveflo.misc;

import java.util.Comparator;
import java.util.Locale;

import android.app.Fragment;
import android.content.Context;

import com.caveflo.dao.Beer;
import com.caveflo.dao.BeerDataSource;
import com.caveflo.dao.BeerReferential;
import com.caveflo.fragment.Alcoolemie;
import com.caveflo.fragment.Cave;
import com.caveflo.fragment.Info;
import com.caveflo.fragment.News;

public class Factory {

	private static final Factory instance = new Factory();

	public static Factory get() {
		return instance;
	}

	private Cave fragmentCave;
	private Fragment fragmentInfo;
	private Fragment fragmentNews;
	private Alcoolemie fragmentAlcoolemie;
	private BeerDataSource beerDataSource;
	private BeerReferential beerReferential;
	private Comparator<Beer> beerComparator;

	public Factory() {
		fragmentCave = new Cave();
		fragmentInfo = new Info();
		fragmentNews = new News();
		fragmentAlcoolemie = new Alcoolemie();
		beerComparator = new Comparator<Beer>() {
			public int compare(Beer lhs, Beer rhs) {
				return lhs.getName().toLowerCase(Locale.getDefault()).compareTo(rhs.getName().toLowerCase(Locale.getDefault()));
			}
		};
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

	public Alcoolemie getFragmentAlcoolemie() {
		return fragmentAlcoolemie;
	}

}
