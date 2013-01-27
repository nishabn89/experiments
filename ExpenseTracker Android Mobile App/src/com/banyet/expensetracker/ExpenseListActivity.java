package com.banyet.expensetracker;

import java.text.DecimalFormat;
import java.util.List;

import android.app.ListActivity;
import android.app.LoaderManager.LoaderCallbacks;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.banyet.expensetracker.model.Expense;
import com.banyet.expensetracker.model.ExpenseTable;
import com.banyet.expensetracker.model.ExpensesContentProvider;

public class ExpenseListActivity extends ListActivity implements LoaderCallbacks<Cursor> {
	public static final String EXPENSE_OBJECT = "com.banyet.expensetracker.EXPENSE_OBJECT";
	private ProgressDialog progressDialog;
	private List<Expense> expenses;
	private ExpensesAdapter expensesAdapter;
	private SimpleCursorAdapter adapter;
	private Runnable viewExpenses;

  private static final int ACTIVITY_CREATE = 0;
  private static final int ACTIVITY_EDIT = 1;
  private static final int DELETE_ID = Menu.FIRST + 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_expense_list);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		ListView listView = getListView();
		listView.setDividerHeight(2);
		fillData();
		registerForContextMenu(listView);
	}

	private void fillData() {
	  // Fields from the database (projection)
    // Must include the _id column for the adapter to work
    String[] from = new String[] { ExpenseTable.COLUMN_NAME };
    int[] to = new int[] { R.id.title };

    getLoaderManager().initLoader(0, null, this);
    adapter = new SimpleCursorAdapter(this, R.layout.row_expense_list, null, from, to, 0);

    setListAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_expense_list, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.expenseList_add:
			createExpense();
			return true;
		case android.R.id.home:
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void createExpense() {
		Intent intent = new Intent(this, NewExpenseActivity.class);
		startActivity(intent);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Intent i = new Intent(this, ExpenseReportActivity.class);
    Uri expenseUri = Uri.parse(ExpensesContentProvider.CONTENT_URI + "/" + id);
    i.putExtra(ExpensesContentProvider.CONTENT_ITEM_TYPE, expenseUri);

    // Activity returns an result if called with startActivityForResult
    startActivityForResult(i, ACTIVITY_EDIT);
	}

	public class ExpensesAdapter extends ArrayAdapter<Expense> {
		private final List<Expense> values;

		public ExpensesAdapter(Context context, int textViewResourceId, List<Expense> items) {
			super(context, textViewResourceId, items);
			this.values = items;
		}

		@Override
		public int getCount() {
			return values.size();
		}

		public Expense getItem(int pos) {
			return values.get(pos);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = getLayoutInflater().inflate(R.layout.row_expense_list, parent, false);
			}

			Expense e = getItem(position);
			TextView tt = (TextView) convertView.findViewById(R.id.title);
			if (tt != null) {
				tt.setText(e.getName());
			}

			TextView bt = (TextView) convertView.findViewById(R.id.subtitle);
			if (bt != null) {
				DecimalFormat df = new DecimalFormat("#.##");
				bt.setText("Amount: " + df.format(e.getAmount()));
			}

			return convertView;
		}
	}

	public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1) {
    adapter.swapCursor(arg1);
	}

	public void onLoaderReset(Loader<Cursor> arg0) {
		adapter.swapCursor(null);
	}

	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
    String[] projection = { ExpenseTable.COLUMN_ID, ExpenseTable.COLUMN_NAME };
    CursorLoader cursorLoader = new CursorLoader(this,
        ExpensesContentProvider.CONTENT_URI, projection, null, null, null);
    return cursorLoader;
	}
}
