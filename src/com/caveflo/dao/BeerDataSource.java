package com.caveflo.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class BeerDataSource {

	private SQLiteDatabase database;
	private BeerSQLiteHelper dbHelper;
	private String[] beerColumns = { BeerSQLiteHelper.TABLE_BEER_COLUMN_ID, BeerSQLiteHelper.TABLE_BEER_COLUMN_NAME, BeerSQLiteHelper.TABLE_BEER_COLUMN_DEGREE, BeerSQLiteHelper.TABLE_BEER_COLUMN_TYPE, BeerSQLiteHelper.TABLE_BEER_COLUMN_COUNTRY, BeerSQLiteHelper.TABLE_BEER_COLUMN_STATUS, BeerSQLiteHelper.TABLE_BEER_COLUMN_CUSTOM };

	private static final String selectCols = BeerSQLiteHelper.TABLE_BEER_COLUMN_ID + ", a." + BeerSQLiteHelper.TABLE_BEER_COLUMN_NAME + ", a." + BeerSQLiteHelper.TABLE_BEER_COLUMN_DEGREE + ", a." + BeerSQLiteHelper.TABLE_BEER_COLUMN_TYPE + ", a." + BeerSQLiteHelper.TABLE_BEER_COLUMN_COUNTRY + ", a." + BeerSQLiteHelper.TABLE_BEER_COLUMN_STATUS + ", a." + BeerSQLiteHelper.TABLE_BEER_COLUMN_CUSTOM + ", b." + BeerSQLiteHelper.TABLE_RATING_COLUMN_RATING + ", b."
			+ BeerSQLiteHelper.TABLE_RATING_COLUMN_DATE + ", b." + BeerSQLiteHelper.TABLE_RATING_COLUMN_COMMENT;
	private static final String selectQuery = "SELECT " + selectCols + " FROM " + BeerSQLiteHelper.TABLE_BEER + " a INNER JOIN " + BeerSQLiteHelper.TABLE_RATING + " b ON a." + BeerSQLiteHelper.TABLE_BEER_COLUMN_ID + "=b." + BeerSQLiteHelper.TABLE_RATING_COLUMN_BEER_ID;

	public BeerDataSource(Context context) {
		dbHelper = new BeerSQLiteHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public Beer createBeer(String id, String name, float degree, String type, String country, int status, int custom) {
		ContentValues values = new ContentValues();
		values.put(BeerSQLiteHelper.TABLE_BEER_COLUMN_ID, id);
		values.put(BeerSQLiteHelper.TABLE_BEER_COLUMN_NAME, name);
		values.put(BeerSQLiteHelper.TABLE_BEER_COLUMN_DEGREE, degree);
		values.put(BeerSQLiteHelper.TABLE_BEER_COLUMN_TYPE, type);
		values.put(BeerSQLiteHelper.TABLE_BEER_COLUMN_COUNTRY, country);
		values.put(BeerSQLiteHelper.TABLE_BEER_COLUMN_STATUS, status);
		values.put(BeerSQLiteHelper.TABLE_BEER_COLUMN_CUSTOM, custom);
		database.insert(BeerSQLiteHelper.TABLE_BEER, null, values);
		Beer beer = getBeer(id);
		return beer;
	}
	
	public long updateBeer(String id, String name, float degree, String type, String country, int status, int custom) {
		ContentValues values = new ContentValues();
		values.put(BeerSQLiteHelper.TABLE_BEER_COLUMN_ID, id);
		values.put(BeerSQLiteHelper.TABLE_BEER_COLUMN_NAME, name);
		values.put(BeerSQLiteHelper.TABLE_BEER_COLUMN_DEGREE, degree);
		values.put(BeerSQLiteHelper.TABLE_BEER_COLUMN_TYPE, type);
		values.put(BeerSQLiteHelper.TABLE_BEER_COLUMN_COUNTRY, country);
		values.put(BeerSQLiteHelper.TABLE_BEER_COLUMN_STATUS, status);
		values.put(BeerSQLiteHelper.TABLE_BEER_COLUMN_CUSTOM, custom);
		return database.update(BeerSQLiteHelper.TABLE_BEER, values, BeerSQLiteHelper.TABLE_BEER_COLUMN_ID + "=?", new String[] {id });
	}

	public long updateBeer(Beer beer) {
		return updateBeer(beer.getId(),beer.getName(),beer.getDegree(),beer.getType(),beer.getCountry(),beer.getStatus(),beer.getCustom() );
	}

	public void deleteBeer(Beer beer) {
		database.delete(BeerSQLiteHelper.TABLE_BEER, BeerSQLiteHelper.TABLE_BEER_COLUMN_ID + "=?", new String[] { beer.getId() });
	}

	public void deleteRating(Beer beer) {
		database.delete(BeerSQLiteHelper.TABLE_RATING, BeerSQLiteHelper.TABLE_RATING_COLUMN_BEER_ID + "=?", new String[] { beer.getId() });
	}

	public Beer getBeer(String id) {
		Cursor cursor = database.rawQuery(selectQuery + " WHERE  " + BeerSQLiteHelper.TABLE_BEER_COLUMN_ID + "=?", new String[] { id });
		cursor.moveToFirst();
		Beer beer = cursorToBeer(cursor);
		cursor.close();
		return beer;
	}

	public List<Beer> getAllBeer() {
		List<Beer> beers = new ArrayList<Beer>();
		Cursor cursor = database.query(BeerSQLiteHelper.TABLE_BEER, beerColumns, null, null, null, null, BeerSQLiteHelper.TABLE_BEER_COLUMN_NAME);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Beer beer = cursorToBeer(cursor);
			beers.add(beer);
			cursor.moveToNext();
		}
		cursor.close();
		return beers;
	}

	public List<Beer> getAllBeerWithRating() {
		List<Beer> beers = new ArrayList<Beer>();
		Cursor cursor = database.rawQuery(selectQuery + " ORDER BY " + BeerSQLiteHelper.TABLE_BEER_COLUMN_NAME, new String[] {});
		while (!cursor.isAfterLast()) {
			Beer beer = cursorToBeer(cursor);
			beers.add(beer);
			cursor.moveToNext();
		}
		cursor.close();
		return beers;
	}

	public int updateRating(Beer beer) {
		return database.update(BeerSQLiteHelper.TABLE_RATING, ratingToContentValues(beer), BeerSQLiteHelper.TABLE_RATING_COLUMN_BEER_ID + "=?", new String[] { beer.getId() });
	}

	public long createRating(Beer beer) {
		return database.insert(BeerSQLiteHelper.TABLE_RATING, null, ratingToContentValues(beer));
	}

	private ContentValues ratingToContentValues(Beer beer) {
		ContentValues values = new ContentValues();
		values.put(BeerSQLiteHelper.TABLE_RATING_COLUMN_BEER_ID, beer.getId());
		values.put(BeerSQLiteHelper.TABLE_RATING_COLUMN_RATING, beer.getRating());
		values.put(BeerSQLiteHelper.TABLE_RATING_COLUMN_COMMENT, beer.getComment());
		values.put(BeerSQLiteHelper.TABLE_RATING_COLUMN_DATE, beer.getRatingDate());
		return values;
	}

	private Beer cursorToBeer(Cursor cursor) {
		Beer beer = new Beer();
		beer.setId(cursor.getString(0));
		beer.setName(cursor.getString(1));
		beer.setDegree(cursor.getFloat(2));
		beer.setType(cursor.getString(3));
		beer.setCountry(cursor.getString(4));
		beer.setStatus(cursor.getInt(5));
		beer.setCustom(cursor.getInt(6));
		if (cursor.getColumnCount() > 5) {
			beer.setRating(cursor.getInt(7));
			beer.setRatingDate(cursor.getString(8));
			beer.setComment(cursor.getString(9));
		}
		return beer;
	}
}
