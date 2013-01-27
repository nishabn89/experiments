package com.banyet.expensetracker.model;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class ExpenseTable {
  public static final String TABLE_EXPENSES = "expenses";
  public static final String COLUMN_ID = "_id";
  public static final String COLUMN_NAME = "name";
  public static final String COLUMN_AMOUNT = "amount";
  public static final String COLUMN_DATE = "date";
  public static final String COLUMN_NOTE = "note";
  public static final String COLUMN_LOCATION = "location";
  public static final String COLUMN_CATEGORY = "category";
  public static final String COLUMN_SHARED_ENTITIES = "shared_entities";

  // Database creation sql statement
  private static final String DATABASE_CREATE = "create table "
      + TABLE_EXPENSES + "("
  		+ COLUMN_ID + " INTEGER primary key autoincrement, "
      + COLUMN_NAME + " TEXT not null, "
      + COLUMN_AMOUNT + " REAL not null, "
      + COLUMN_DATE + " TEXT not null, "
      + COLUMN_NOTE + " TEXT, "
      + COLUMN_LOCATION + " TEXT, "
      + COLUMN_CATEGORY + " TEXT"
      //+ COLUMN_SHARED_ENTITIES + " TEXT not null"
  		+");";

  public static void onCreate(SQLiteDatabase database) {
    database.execSQL(DATABASE_CREATE);
  }

  public static void onUpgrade(SQLiteDatabase database, int oldVersion,
      int newVersion) {
    Log.w(ExpenseTable.class.getName(), "Upgrading database from version "
        + oldVersion + " to " + newVersion
        + ", which will destroy all old data");
    database.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPENSES);
    onCreate(database);
  }
}
