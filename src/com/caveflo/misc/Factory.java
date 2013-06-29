package com.caveflo.misc;

import java.util.Comparator;

import android.app.Fragment;

import com.caveflo.cave.Biere;
import com.caveflo.cave.CaveDB;
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

	private CaveDB caveDB;

	private Comparator<Biere> biereComparator;

	public Factory() {
		fragmentCave = new Cave();
		fragmentInfo = new Info();
		fragmentNews = new News();
		caveDB = new CaveDB();
		biereComparator = new Comparator<Biere>() {
			public int compare(Biere lhs, Biere rhs) {
				return lhs.getName().toLowerCase().compareTo(rhs.getName().toLowerCase());
			}
		};
	}

	public CaveDB getCaveDB() {
		return caveDB;
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

	public Comparator<Biere> getBiereComparator() {
		return biereComparator;
	}
}
