package com.banyet.expensetracker;

import android.util.Log;
import android.os.Bundle;
import android.app.Activity;
import android.location.Criteria;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Context;
import android.widget.Toast;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.view.View;
import android.view.View.OnClickListener;
import android.content.Intent;

public class MapActivity extends Activity implements LocationListener {

	Location mostRecentLocation;
	String TAG = "Activity";
	private WebView webView;
	private String URL;
	private static final String MAP_URL = "file:///android_asset/map.html";
	public static double latitude = 0;
	public static double longitude = 0;
	LocationManager locationManager;
	String bestProvider;
	Button locButton;
	Button clrButton;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		addListenerOnButton();
		getLocation();
		setupWebView();
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
	
	public void addListenerOnButton() {
		 
		locButton = (Button) findViewById(R.id.button1);
 
		locButton.setOnClickListener(new OnClickListener() {
 
			@Override
			public void onClick(View arg0) {
				Log.d("debug click", mostRecentLocation.toString());
				final String URL1 = "javascript: drop("+ mostRecentLocation.getLatitude() + "," 
						+ mostRecentLocation.getLongitude()+")";
				 webView.loadUrl(URL1);
			}
		});
		
		clrButton = (Button) findViewById(R.id.button2);
		 
		clrButton.setOnClickListener(new OnClickListener() {
 
			@Override
			public void onClick(View arg0) {
				
				URL = "javascript: clearMarkers()";
				
				      webView.loadUrl(URL);
			}
		});
	}

	private void getLocation() {
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		bestProvider = locationManager.getBestProvider(criteria, true);
		// In order to make sure the device is getting the location, request
		// updates.
		Log.d(TAG, bestProvider);
		locationManager.requestLocationUpdates(bestProvider, 1, 0, this);
		mostRecentLocation = locationManager.getLastKnownLocation(bestProvider);
		
	}

	@Override
	public void onLocationChanged(Location loc) {
		mostRecentLocation = loc;
	}

	/** Sets up the WebView object and loads the URL of the page **/

	private void setupWebView() {
		webView = (WebView) findViewById(R.id.webview);
		webView.getSettings().setJavaScriptEnabled(true);
		// Wait for the page to load then send the location information
		// webView.setWebViewClient(new WebViewClient());
		
		webView.loadUrl(MAP_URL);
//		Log.d("debug", mostRecentLocation.toString());
		webView.addJavascriptInterface(new JavaScriptInterface(), "android");
	}

	private class JavaScriptInterface {
		  public double getLatitude(){
		    return mostRecentLocation.getLatitude();
		  }
		  public double getLongitude(){
		    return mostRecentLocation.getLongitude();
		  }
		}
	@Override
	protected void onResume(){
		super.onResume();
		locationManager.requestLocationUpdates(bestProvider, 20000, 1, this);
		mostRecentLocation = locationManager.getLastKnownLocation(bestProvider);
		addListenerOnButton();
	}
	
	@Override
	protected void onPause(){
		super.onPause();
		locationManager.removeUpdates(this);
	}
	@Override
	public void onProviderDisabled(String provider) {
		// print "Currently GPS is Disabled";
	}

	@Override
	public void onProviderEnabled(String provider) {
		// print "GPS got Enabled";

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {

	}

}
