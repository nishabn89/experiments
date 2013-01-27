package com.banyet.expensetracker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.banyet.expensetracker.model.sharedBillFriends;

public class AddFriendsShareActivity extends Activity {
	private ArrayList<sharedBillFriends> sharedFriends = new ArrayList<sharedBillFriends>();
	private sharedBillFriendsAdapter adapter;
	private MenuItem editFriendsInfo;
	public HashMap<String, Double> checked = new HashMap<String, Double>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_friends_share);

		sharedBillFriends friend1 = new sharedBillFriends("Noreen", 10.0);
		sharedFriends.add(friend1);
		sharedBillFriends friend2 = new sharedBillFriends("Li", 10.0);
		sharedFriends.add(friend2);
		sharedBillFriends friend3 = new sharedBillFriends("Lionel", 10.0);
		sharedFriends.add(friend3);

		ListView listView = (ListView) findViewById(R.id.addSharedFriendsList);
		adapter = new sharedBillFriendsAdapter(this,
				android.R.layout.simple_list_item_1, sharedFriends);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {
			// arg0 -> adapter view; arg1->view; arg2->position; arg3->id
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				setCheckedItem(arg2);
				if (checked.size() == 1) {
					editFriendsInfo.setVisible(true);
				} else if (checked.size() != 1) {
					editFriendsInfo.setVisible(false);
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_add_friends_share, menu);
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		MenuItem item = menu.findItem(R.id.friendsShareList_edit);
		editFriendsInfo = menu.findItem(R.id.friendsShareList_edit);
		if (checked.size() != 1) {
			item.setVisible(false);
		}
		item = menu.findItem(R.id.friendsShareList_add);
		item.setVisible(true);
		item = menu.findItem(R.id.friendsShareList_delete);
		item.setVisible(true);
		return true;
	}

	public void launchAddMoreFriends(final Context c) {
		Intent intent = new Intent(this, NewShareFriend.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
		startActivityForResult(intent, 1);
	}
	
	public void launchEditFriends(final Context c){
		Intent intent = new Intent(this, NewShareFriend.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
		Iterator<String> itr = checked.keySet().iterator();
		String key = "";
		while(itr.hasNext()){
			key = itr.next();
		}
		sharedBillFriends sb = new sharedBillFriends(key, checked.get(key));
		intent.putExtra("EDIT_FRIENDS_DATA", sb);
		startActivityForResult(intent, 2);
		
	}

	public void launchDeleteFriends(final Context c) {

		Iterator<String> it = checked.keySet().iterator();
		while (it.hasNext()) {
			String itemToDel = (String) it.next();
			for (int i = 0; i < sharedFriends.size(); i++) {
				if (sharedFriends.get(i).getName().equals(itemToDel)) {
					sharedFriends.remove(i);
				}

			}
		}
		adapter.notifyDataSetChanged();
		ListView listView = (ListView) findViewById(R.id.addSharedFriendsList);
		for (int i = 0; i < sharedFriends.size(); i++) {
			listView.setItemChecked(i, false);
		}
		checked.clear();
	}

	public void setCheckedItem(int position) {
		sharedBillFriends sbf = sharedFriends.get(position);
		if (checked.containsKey(sbf.getName())) {
			checked.remove(sbf.getName());
		} else {
			checked.put(sbf.getName(), sbf.getAmount());
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		sharedBillFriends rec = (sharedBillFriends) data
				.getSerializableExtra("RESULT_DATA_ADDED");
		if(requestCode==1){
			sharedFriends.add(rec);
		}
		else if(requestCode==2){
			Iterator<String> itr = checked.keySet().iterator();
			String key = "";
			while(itr.hasNext()){
				key = itr.next();
			}
			for(int i = 0; i < sharedFriends.size();i++){
				if(sharedFriends.get(i).getName().equals(key)){
					sharedFriends.get(i).setName(rec.getName());
					sharedFriends.get(i).setAmount(rec.getAmount());
				}
			}
			checked.clear();
		}
		adapter.notifyDataSetChanged();
		ListView listView = (ListView) findViewById(R.id.addSharedFriendsList);
		for (int i = 0; i < sharedFriends.size(); i++) {
			listView.setItemChecked(i, false);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.friendsShareList_add:
			launchAddMoreFriends(this);
			return true;
		case R.id.friendsShareList_delete:
			launchDeleteFriends(this);
			return true;
		case R.id.friendsShareList_edit:
			launchEditFriends(this);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void doneAddingFriends(View v) {
		Intent in = new Intent(this, sharedBillFriends.class);
		in.putExtra("RESULT_DATA_ADDED", sharedFriends);
		setResult(1, in);
		finish();
	}

}
