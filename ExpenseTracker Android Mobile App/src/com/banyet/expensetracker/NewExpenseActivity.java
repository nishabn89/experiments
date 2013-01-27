package com.banyet.expensetracker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.banyet.expensetracker.model.ExpenseTable;
import com.banyet.expensetracker.model.ExpensesContentProvider;

public class NewExpenseActivity extends FragmentActivity {
	public static final String EXPENSE_OBJECT = "com.banyet.expensetracker.EXPENSE_OBJECT";
//	private Expense expense;
	EditText expenseName;
	EditText expenseAmount;
	EditText expenseDate;
	Spinner expenseCategory;
	EditText expenseLocation;
	EditText expenseNote;
	private Uri expenseUri;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_new_expense);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setTitle("New Expense");

		expenseName = (EditText) findViewById(R.id.newExpense_editName);
		expenseAmount = (EditText) findViewById(R.id.newExpense_editAmount);
		expenseDate = (EditText) findViewById(R.id.newExpense_editDate);
		expenseCategory = (Spinner) findViewById(R.id.newExpense_spinnerCategory);
		expenseLocation = (EditText) findViewById(R.id.newExpense_editLocation);
		expenseNote = (EditText) findViewById(R.id.newExpense_note);

		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.category_array,
				android.R.layout.simple_spinner_item);

		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		expenseCategory.setAdapter(adapter);
		

		Bundle extras = getIntent().getExtras();
		// Check from the saved Instance
		expenseUri = (savedInstanceState == null) ? null
				: (Uri) savedInstanceState.getParcelable(ExpensesContentProvider.CONTENT_ITEM_TYPE);

		// Or passed from the other activity
		if (extras != null) {
			expenseUri = extras
					.getParcelable(ExpensesContentProvider.CONTENT_ITEM_TYPE);

			fillData(expenseUri);
		}
	}

	private void fillData(Uri uri) {
		String[] projection = {
				ExpenseTable.COLUMN_NAME,
				ExpenseTable.COLUMN_AMOUNT,
				ExpenseTable.COLUMN_DATE,
				ExpenseTable.COLUMN_CATEGORY,
				ExpenseTable.COLUMN_LOCATION,
				ExpenseTable.COLUMN_NOTE };

		Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
		if (cursor != null) {
			cursor.moveToFirst();
			expenseName.setText(cursor.getString(cursor.getColumnIndexOrThrow(ExpenseTable.COLUMN_NAME)));
			expenseAmount.setText(cursor.getString(cursor.getColumnIndexOrThrow(ExpenseTable.COLUMN_AMOUNT)));
			expenseDate.setText(cursor.getString(cursor.getColumnIndexOrThrow(ExpenseTable.COLUMN_DATE)));
			expenseLocation.setText(cursor.getString(cursor.getColumnIndexOrThrow(ExpenseTable.COLUMN_LOCATION)));
			expenseNote.setText(cursor.getString(cursor.getColumnIndexOrThrow(ExpenseTable.COLUMN_NOTE)));

			String category = cursor.getString(cursor.getColumnIndexOrThrow(ExpenseTable.COLUMN_CATEGORY));
			for (int i = 0; i < expenseCategory.getCount(); i++) {
				String s = (String) expenseCategory.getItemAtPosition(i);
				if (s.equalsIgnoreCase(category)) {
					expenseCategory.setSelection(i);
				}
			}
			cursor.close(); // Always close the cursor
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void showDatePickerDialog(View v) {
		final Calendar c = Calendar.getInstance();
		DatePickerDialog date = new DatePickerDialog(this,
				new OnDateSetListener() {
					public void onDateSet(DatePicker view, int year,
							int monthOfYear, int dayOfMonth) {
						EditText editText = (EditText) findViewById(R.id.newExpense_editDate);
						editText.setText(monthOfYear+1 + "/" + dayOfMonth + "/"
								+ year);
					}
				}, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c
						.get(Calendar.DAY_OF_MONTH));
		date.show();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		saveState();
		outState.putParcelable(ExpensesContentProvider.CONTENT_ITEM_TYPE,
				expenseUri);
	}

	@Override
	protected void onPause() {
		super.onPause();
		saveState();
	}

	private void saveState() {
		String name = expenseName.getText().toString();
		String category = (String) expenseCategory.getSelectedItem();
		String amount = expenseAmount.getText().toString();

		// Only save if either summary or description is available
		if (amount.length() == 0 && name.length() == 0) {
			return;
		}

		ContentValues values = new ContentValues();
		values.put(ExpenseTable.COLUMN_NAME, name);
		values.put(ExpenseTable.COLUMN_CATEGORY, category);
		values.put(ExpenseTable.COLUMN_AMOUNT, Double.parseDouble(amount));
		values.put(ExpenseTable.COLUMN_NOTE, expenseNote.getText().toString());
		values.put(ExpenseTable.COLUMN_LOCATION, expenseLocation.getText().toString());

		try {
			SimpleDateFormat dfFrom = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
			SimpleDateFormat dfTo = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
			Date dt = dfFrom.parse(expenseDate.getText().toString());
			values.put(ExpenseTable.COLUMN_DATE, dfTo.format(dt));
		} catch (Exception e) {
			Log.v("Error", "Error parsing date from string.");
		}

		if (expenseUri == null) {
			expenseUri = getContentResolver().insert(
					ExpensesContentProvider.CONTENT_URI, values);
		} else {
			getContentResolver().update(expenseUri, values, null, null);
		}
	}

	public void onClick_Location(View v){
		Intent intent = new Intent(this, MapActivity.class);
		startActivityForResult(intent, 1);
	}

	public void launchShareBill(View view) {
		Intent intent = new Intent(this, AddFriendsShareActivity.class);
		startActivity(intent);
	}

	public void launchExpenseReport(View view) {
		// Do something in response to button
		saveState();

		Intent intent = new Intent(this, ExpenseReportActivity.class);
    intent.putExtra(ExpensesContentProvider.CONTENT_ITEM_TYPE, expenseUri);

    // Activity returns an result if called with startActivityForResult
    startActivity(intent);
		finish();
	}
}
