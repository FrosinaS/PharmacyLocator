package mk.finki.ukim.jmm.pharmacylocator;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class PharmacyInfo extends FragmentActivity {
	private GoogleMap map;
	MarkerOptions venueMarkerOptions;
	GPSTracker gps;
	Double lat;
	Double lng;
	TextView pharmacyPhone;
	Button callButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pharmacy_info_layout);
		TextView pharmacyName = (TextView) findViewById(R.id.pharmacy_info_name);
		TextView pharmacyAdress = (TextView) findViewById(R.id.pharmacy_info_address);
		TextView pharmacyCity = (TextView) findViewById(R.id.pharmacy_info_city);
		pharmacyPhone = (TextView) findViewById(R.id.pharmacy_info_phone);
		callButton = (Button) findViewById(R.id.button1);
		Intent newView = getIntent();
		if (!newView.getStringExtra("PHARMACY_PHONE").equals("/")) {
			callButton.setEnabled(true);
		}
		pharmacyName.setText(newView.getStringExtra("PHARMACY_NAME"));
		pharmacyAdress.setText(newView.getStringExtra("PHARMACY_ADDRESS"));
		pharmacyCity.setText(newView.getStringExtra("PHARMACY_CITY"));
		pharmacyPhone.setText(newView.getStringExtra("PHARMACY_PHONE"));
		lat = newView.getDoubleExtra("PHARMACY_LAT", 0.0);
		lng = newView.getDoubleExtra("PHARMACY_LNG", 0.0);

		callButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent callIntent = new Intent(Intent.ACTION_DIAL);
				callIntent.setData(Uri.parse("tel:"
						+ pharmacyPhone.getText().toString()));
				startActivity(callIntent);
			}
		});

		MapsInitializer.initialize(getApplicationContext());
		switch (GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(getApplicationContext())) {

		case ConnectionResult.SUCCESS:

			map = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map_view)).getMap();
			map.setMyLocationEnabled(true);
			map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
			venueMarkerOptions = new MarkerOptions();
			venueMarkerOptions.position(new LatLng(lat, lng));
			map.addMarker(venueMarkerOptions).setIcon(
					BitmapDescriptorFactory
							.defaultMarker(BitmapDescriptorFactory.HUE_RED));
			map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat,
					lng), 20));

			break;

		case ConnectionResult.SERVICE_MISSING:
			Toast.makeText(getApplicationContext(), "SERVICE MISSING",
					Toast.LENGTH_SHORT).show();
			break;

		case ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED:
			Toast.makeText(getApplicationContext(), "UPDATE REQUIRED",
					Toast.LENGTH_SHORT).show();
			break;

		default:
			Log.e("MapViewFragment",
					"Google service play available code"
							+ GooglePlayServicesUtil
									.isGooglePlayServicesAvailable(getApplicationContext()));
		}
		
	}

	

	public void onConfigurationChanged(Configuration newConfig)

	{

		setContentView(R.layout.pharmacy_info_layout);

		super.onConfigurationChanged(newConfig);

		callButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent callIntent = new Intent(Intent.ACTION_DIAL);
				callIntent.setData(Uri.parse("tel:"
						+ pharmacyPhone.getText().toString()));
				startActivity(callIntent);
			}
		});

	}

}
