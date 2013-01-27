package com.banyet.expensetracker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.banyet.expensetracker.model.Expense;
import com.banyet.expensetracker.model.sharedBillFriends;

public class NewShareFriend extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_share_friend);
        Intent intent = getIntent();
        sharedBillFriends sb = (sharedBillFriends) intent
				.getSerializableExtra("EDIT_FRIENDS_DATA");
        if (sb == null) {
			sb = new sharedBillFriends("",0);
		}
        if(!sb.getName().equals("")){
        	EditText editText = (EditText) findViewById(R.id.addFriendsShareName);
        	editText.setText(sb.getName());
        }
        EditText editText = (EditText)  findViewById(R.id.addFriendsShareAmt);
        editText.setText(Double.toString(sb.getAmount()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_new_share_friend, menu);
        return true;
    }
    
    public void callmoreFriendsAdded(View v){
    	Context context = getApplicationContext();
    	TextView textViewaName = (TextView) findViewById(R.id.addFriendsShareName);
    	String textName = textViewaName.getText().toString();
    	TextView textViewAmt = (TextView) findViewById(R.id.addFriendsShareAmt);
    	String strtextAmt = textViewAmt.getText().toString();
    	if(textName.equals("") && strtextAmt.equals("") ){
	       	CharSequence text = "Please enter name and amount!";
	        int duration = Toast.LENGTH_SHORT;
	    	Toast toast = Toast.makeText(context, text, duration);
	    	toast.show();
    	} else if (strtextAmt.equals("") ) {
    		CharSequence text = "Please enter amount!";
	        int duration = Toast.LENGTH_SHORT;
	    	Toast toast = Toast.makeText(context, text, duration);
	    	toast.show();
    	}else if (textName.equals("") ) {
    		CharSequence text = "Please enter name!";
	        int duration = Toast.LENGTH_SHORT;
	    	Toast toast = Toast.makeText(context, text, duration);
	    	toast.show();
    	}else {
    		double textAmt = Double.parseDouble(strtextAmt);
	    	sharedBillFriends newRec = new sharedBillFriends(textName,textAmt);
	    	Intent in = new Intent(this,sharedBillFriends.class);
	    	in.putExtra("RESULT_DATA_ADDED", newRec);
	    	setResult(1, in);
	    	finish();
    	}
     }
}
