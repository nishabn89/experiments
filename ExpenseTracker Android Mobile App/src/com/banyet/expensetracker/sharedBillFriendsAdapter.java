package com.banyet.expensetracker;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.banyet.expensetracker.model.sharedBillFriends;

public class sharedBillFriendsAdapter extends ArrayAdapter<sharedBillFriends> {
	private ArrayList<sharedBillFriends> users;
//	public HashMap<String, Double> checked = new HashMap<String, Double>();

	public sharedBillFriendsAdapter(Context context, int textViewResourceId,
			ArrayList<sharedBillFriends> users) {
		super(context, textViewResourceId, users);
		this.users = users;
	}

//	public void setCheckedItem(int position) {
//		sharedBillFriends sbf = users.get(position);
//		if (checked.containsKey(sbf.getName())) {
//			checked.remove(sbf.getName());
//		} else {
//			checked.put(sbf.getName(), sbf.getAmount());
//		}
//	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if (v == null) {
			LayoutInflater vi = (LayoutInflater) this.getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.checkable_linear_layout, null);
		}

		sharedBillFriends user = users.get(position);
		if (user != null) {
			TextView name = (TextView) v.findViewById(R.id.friends_checkbox);
			TextView amount = (TextView) v.findViewById(R.id.friends_info);

			if (name != null) {
				name.setText(user.getName());
			}

			if (amount != null) {
				amount.setText("Amount: " + user.getAmount());
			}
		}
		return v;
	}

}