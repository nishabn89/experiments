package com.banyet.expensetracker.model;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ExpenseDatabaseHelper extends SQLiteOpenHelper {
  private static final String DATABASE_NAME = "expenses.db";
  private static final int DATABASE_VERSION = 1;

  public ExpenseDatabaseHelper(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase database) {
    ExpenseTable.onCreate(database);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
  	ExpenseTable.onUpgrade(db, oldVersion, newVersion);
  }
} 