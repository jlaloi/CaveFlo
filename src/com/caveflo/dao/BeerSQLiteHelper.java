package com.caveflo.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class BeerSQLiteHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "beers.db";
	private static final int DATABASE_VERSION = 1;

	public static final String TABLE_BEER = "beer";
	public static final String TABLE_BEER_COLUMN_ID = "id";
	public static final String TABLE_BEER_COLUMN_NAME = "name";
	public static final String TABLE_BEER_COLUMN_DEGREE = "degree";
	public static final String TABLE_BEER_COLUMN_TYPE = "type";
	public static final String TABLE_BEER_COLUMN_COUNTRY = "country";
	public static final String TABLE_BEER_COLUMN_STATUS = "status";
	public static final String TABLE_BEER_COLUMN_CUSTOM = "custom";

	public static final String TABLE_RATING = "rating";
	public static final String TABLE_RATING_COLUMN_BEER_ID = "beer_id";
	public static final String TABLE_RATING_COLUMN_RATING = "rating";
	public static final String TABLE_RATING_COLUMN_COMMENT = "comment";
	public static final String TABLE_RATING_COLUMN_DATE = "date";

	private static final String DATABASE_CREATE_BEER = "create table " + TABLE_BEER + "(" + TABLE_BEER_COLUMN_ID + " VARCHAR(20), " + TABLE_BEER_COLUMN_NAME + " VARCHAR(100)," + TABLE_BEER_COLUMN_DEGREE + " float" + TABLE_BEER_COLUMN_TYPE + " VARCHAR(100)," + TABLE_BEER_COLUMN_COUNTRY + " VARCHAR(100)," + TABLE_BEER_COLUMN_STATUS + " integer DEFAULT 1," + TABLE_BEER_COLUMN_CUSTOM + " integer DEFAULT 1 );";
	private static final String DATABASE_CREATE_RATING = "create table " + TABLE_RATING + "(" + TABLE_RATING_COLUMN_BEER_ID + " VARCHAR(20)," + TABLE_RATING_COLUMN_RATING + " integer" + TABLE_RATING_COLUMN_COMMENT + " text," + TABLE_RATING_COLUMN_DATE + " VARCHAR(10) );";

	public BeerSQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE_BEER);
		database.execSQL(DATABASE_CREATE_RATING);
	}

	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(BeerSQLiteHelper.class.getName(), "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_BEER);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_RATING);
		onCreate(db);
	}

}