package com.banyet.expensetracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

public class MainActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
//		LocationManager manager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	public void launchNewExpense(View view) {
		Intent intent = new Intent(this, NewExpenseActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
		startActivity(intent);
	}

	public void launchExpenseList(View view) {
		Intent intent = new Intent(this, ExpenseListActivity.class);
		startActivity(intent);
	}
	
//	public void launchShareFriends(View view) {
//		Intent intent = new Intent(this, AddFriendsShareActivity.class);
//		startActivity(intent);
//	}

	public void launchMaps(View view) {
		Intent intent = new Intent(this, MapActivity.class);
		startActivity(intent);
	}
	
	public void launchFilter(View v){
		Intent intent = new Intent(this, SortFilterActivity.class);
		startActivity(intent);
	}
}
