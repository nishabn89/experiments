package com.banyet.expensetracker;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.banyet.expensetracker.model.Expense;
import com.banyet.expensetracker.model.ExpenseTable;
import com.banyet.expensetracker.model.ExpensesContentProvider;

public class ExpenseReportActivity extends FragmentActivity {	
	public final static String EXPENSE_OBJECT = "com.banyet.expensetracker.EXPENSE_OBJECT";
	private TextView expenseName;
	private TextView expenseAmount;
	private TextView expenseDate;
	private TextView expenseCategory;
	private TextView expenseLocation;
	private TextView expenseNote;
	private Uri expenseUri;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_expense_report);
		getActionBar().setDisplayHomeAsUpEnabled(true);

//		Intent intent = getIntent();
//		expense = (Expense) intent
//				.getSerializableExtra(NewExpenseActivity.EXPENSE_OBJECT);

    expenseName = (TextView) findViewById(R.id.expenseRep_expenseName);
  	expenseAmount = (TextView) findViewById(R.id.expenseRep_expenseAmount);
  	expenseDate =(TextView) findViewById(R.id.expenseRep_expenseDate);
  	expenseCategory = (TextView) findViewById(R.id.expenseRep_expenseCategory);
  	expenseLocation = (TextView) findViewById(R.id.expenseRep_expenseLocation);
  	expenseNote = (TextView) findViewById(R.id.expenseRep_expenseNote);

		ListView listView = (ListView) findViewById(R.id.mylist);
		String[] values = new String[] { "Nisha", "Noreen", "Lionel", "Shelly" };
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, values);
		listView.setAdapter(adapter);

		Bundle extras = getIntent().getExtras();
		expenseUri = (savedInstanceState == null) ? null : (Uri) savedInstanceState
        .getParcelable(ExpensesContentProvider.CONTENT_ITEM_TYPE);

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

		Cursor c = getContentResolver().query(uri, projection, null, null, null);
		if (c != null) {
			c.moveToFirst();
			expenseName.setText(c.getString(c.getColumnIndexOrThrow(ExpenseTable.COLUMN_NAME)));
			expenseAmount.setText(c.getString(c.getColumnIndexOrThrow(ExpenseTable.COLUMN_AMOUNT)));
	  	expenseDate.setText(c.getString(c.getColumnIndexOrThrow(ExpenseTable.COLUMN_DATE)));
	  	expenseCategory.setText(c.getString(c.getColumnIndexOrThrow(ExpenseTable.COLUMN_CATEGORY)));
			expenseLocation.setText(c.getString(c.getColumnIndexOrThrow(ExpenseTable.COLUMN_LOCATION)));
			expenseNote.setText(c.getString(c.getColumnIndexOrThrow(ExpenseTable.COLUMN_NOTE)));
			c.close(); // Always close the cursor
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.expenseRep_deleteExpense:
			deleteExpense(this);
			return true;
		case R.id.expenseRep_editExpense:
			editExpense();
		case android.R.id.home:
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_expense_report, menu);
		return true;
	}

	public void deleteExpense(final Context c) {
		AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setTitle("Delete Expense");
		alertDialog.setMessage("Are you sure?");
		alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE,"Cancel", new OnClickListener(){
			public void onClick(DialogInterface dialog, int which){
				
			}
		});
		alertDialog.setButton(DialogInterface.BUTTON_POSITIVE,"Yes", new OnClickListener(){
			public void onClick(DialogInterface dialog, int which){
				finish();
			}
		});
		alertDialog.show();
	}
	
	public void editExpense(){
		Intent intent = new Intent(this, NewExpenseActivity.class);
    intent.putExtra(ExpensesContentProvider.CONTENT_ITEM_TYPE, expenseUri);
    startActivity(intent);
	}
}
