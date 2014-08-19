package mk.finki.ukim.jmm.pharmacylocator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

public class GlobalView extends Activity {
	public static ProgressDialog dialog;
	public static double latitude;
	public static double longitude;
	public static Location location1;
	ListView list;
	public static ArrayList<Pharmacy> pharmaciesList;
	public static PharmacyAdapter pharmacyAdapter;
	public static Button retryButton;
	// /GetPharmacies task;

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		super.onCreate(savedInstanceState);
		Intent newView = getIntent();
		setContentView(R.layout.global_view);
		
		AdView adView=(AdView)findViewById(R.id.adView);
		AdRequest request=new AdRequest.Builder().build();
		adView.loadAd(request);
		// task = new GetPharmacies();
		retryButton=(Button)findViewById(R.id.retry_button);
		retryButton.setOnClickListener(retryButtonOnClick);
		String typeFragment = newView.getStringExtra("FRAGMENT_TYPE");
		if (savedInstanceState == null) {
			if (typeFragment.equals("search nearest")) {
				FragmentManager fragmentManager = getFragmentManager();
				FragmentTransaction fragmentTransaction = fragmentManager
						.beginTransaction();
				fragmentTransaction.add(R.id.global_fragment_layout,
						new SearchNearest());
				fragmentTransaction.commit();
			} else if (typeFragment.equals("search pharmacy")) {
				FragmentManager fragmentManager = getFragmentManager();
				FragmentTransaction fragmentTransaction = fragmentManager
						.beginTransaction();
				fragmentTransaction.add(R.id.global_fragment_layout,
						new SearchPharmacy());
				fragmentTransaction.commit();
			}
			pharmaciesList = new ArrayList<Pharmacy>();
			latitude = 41.5178;
			longitude = 20.9656;
			location1 = new Location("curr");
			dialog = new ProgressDialog(GlobalView.this);
			dialog.setCancelable(false);
			dialog.setMessage("Searching for pharmacies...");
			dialog.isIndeterminate();

			GPSTracker gps = new GPSTracker(getApplicationContext());
			if (gps.canGetLocation) {
				location1.setLatitude(gps.latitude);
				location1.setLongitude(gps.longitude);
				gps.stopUsingGPS();
			}

			getPh(GlobalView.this);

		}
		pharmacyAdapter = new PharmacyAdapter(this.getApplicationContext(),
				R.layout.pharmacy_item, pharmaciesList);
		list = (ListView) findViewById(R.id.pharmacy_list_view);
		list.setAdapter(pharmacyAdapter);

		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Intent pharmacyView = new Intent(getApplicationContext(),
						PharmacyInfo.class);
				pharmacyView.putExtra("PHARMACY_NAME",
						pharmacyAdapter.pharmacies.get(position)
								.getPharmacyName());
				pharmacyView.putExtra("PHARMACY_ADDRESS",
						pharmacyAdapter.pharmacies.get(position)
								.getPharmacyAdress());
				pharmacyView.putExtra("PHARMACY_CITY",
						pharmacyAdapter.pharmacies.get(position)
								.getPharmacyCity());
				pharmacyView.putExtra("PHARMACY_PHONE",
						pharmacyAdapter.pharmacies.get(position)
								.getPharmacyTelephoneNumber());
				Double lat = pharmacyAdapter.pharmacies.get(position)
						.getPharmacyLatitude();
				Double lng = pharmacyAdapter.pharmacies.get(position)
						.getPharmacyLongitude();
				pharmacyView.putExtra("PHARMACY_LAT", lat);
				pharmacyView.putExtra("PHARMACY_LNG", lng);
				startActivity(pharmacyView);

			}
		});
		 
	}
	private OnClickListener retryButtonOnClick=new OnClickListener(){

		@Override
		public void onClick(View v) {
			getPh(GlobalView.this);
		}
    	
    	
    };


	@Override
	protected void onStop() {
		super.onStop();
		if (dialog != null) {
			dialog.dismiss();
			// dialog = null;
		}
	}

	public static void getPh(Context context) {
		context.startService(new Intent(context, SearchPharmaciesService.class));
	}

	public static double distanceKM(double lat1, double lon1, double lat2,
			double lon2) {
		double dist = 0.0;
		String url = "https://maps.googleapis.com/maps/api/distancematrix/json?origins="
				+ lat1
				+ ","
				+ lon1
				+ "&destinations="
				+ lat2
				+ ","
				+ lon2
				+ "&mode=driving&key=AIzaSyATKyvhWyatHyHRh8JxonJwc_vahwq7oBY";

		String response = httpRequest(url);
		try {
			dist = 0.1;
			JSONObject jsonObj = new JSONObject(response);
			dist = 0.2;
			JSONArray data = (JSONArray) jsonObj.getJSONArray("rows");
			dist = 0.3;
			// //
			JSONObject elementsParent = data.getJSONObject(0);
			dist = 0.4;
			JSONArray elements = (JSONArray) elementsParent
					.getJSONArray("elements");
			JSONObject distanceParent = elements.getJSONObject(0);
			JSONObject distance = distanceParent.getJSONObject("distance");
			dist = 0.5;
			String rast = distance.getString("text");
			String d = rast.split(" ")[0];
			dist = Double.parseDouble(d.replace(",", ""));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return dist;

	}

	protected static String httpRequest(String url) {

		String line = "";
		String responseResult = "";

		HttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(url);

		try {

			HttpResponse response = client.execute(get);

			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();

			if (statusCode == 200) {
				BufferedReader rd = new BufferedReader(new InputStreamReader(
						response.getEntity().getContent()));
				while ((line = rd.readLine()) != null) {
					responseResult += line;
				}
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return responseResult;
	}

}
