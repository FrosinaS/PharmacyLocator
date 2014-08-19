package mk.finki.ukim.jmm.pharmacylocator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.ProgressDialog;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

public class PharmaciesMap extends FragmentActivity {
	private GoogleMap map;
	ArrayList<Pharmacy> pharm;
	Location location1;
	ProgressDialog dialog;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pharmacies_map);
		
		
		if(savedInstanceState==null){
			PharmacyList task = new PharmacyList();
			pharm = new ArrayList<Pharmacy>();
			location1 = new Location("Map");
			GPSTracker gps = new GPSTracker(getApplicationContext());
			if (gps.canGetLocation) {
				location1.setLatitude(gps.latitude);
				location1.setLongitude(gps.longitude);
				gps.stopUsingGPS();
			}
			
		dialog = new ProgressDialog(PharmaciesMap.this);
		dialog.setCancelable(false);
		dialog.setMessage("Searching for pharmacies...");
		dialog.isIndeterminate();

		task.execute();
		
		MapsInitializer.initialize(getApplicationContext());
		switch (GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(getApplicationContext())) {

		case ConnectionResult.SUCCESS:

			map = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.pharmacies)).getMap();
			map.setMyLocationEnabled(true);
			map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
			MarkerOptions venueMarkerOptions = new MarkerOptions()
					.title("You are here!");
			venueMarkerOptions.position(new LatLng(location1.getLatitude(),
					location1.getLongitude()));
			map.addMarker(venueMarkerOptions).setIcon(
					BitmapDescriptorFactory
							.defaultMarker(BitmapDescriptorFactory.HUE_RED));
			map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
					location1.getLatitude(), location1.getLongitude()), 20));

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

		
	}

	@Override
	protected void onStop() {
		super.onStop();
		if (dialog != null) {
			dialog.dismiss();
			// dialog = null;
		}
	}

	String urlA;

	public class PharmacyList extends
			AsyncTask<Void, Void, ArrayList<Pharmacy>> {

		@Override
		protected ArrayList<Pharmacy> doInBackground(Void... params) {

			urlA = "https://api.foursquare.com/v2/venues/search?ll="
					+ location1.getLatitude()
					+ ","
					+ location1.getLongitude()
					+ "&categoryId=4bf58dd8d48988d10f951735"
					+ "&limit=50&oauth_token=KV3WTKON301SAXW1HNF4BC40WVRARJL2OO5UTV0SDIP5NMPA&v=20140506";
			String response = httpRequest(urlA);
			SearchData dataProcesor = new SearchData();
			return (ArrayList<Pharmacy>) dataProcesor.parseSearchJSON(response);
		}

		@Override
		protected void onPostExecute(ArrayList<Pharmacy> pharmacies) {
			super.onPostExecute(pharmacies);

			if (dialog.isShowing()) {
				dialog.dismiss();
				// GlobalView.dialog = null;

			}
			if(pharmacies != null){
			pharm.clear();
			pharm.addAll(pharmacies);

			for (Pharmacy pharmacy : pharm) {

				Location location2 = new Location("Point B");
				location2.setLatitude(pharmacy.getPharmacyLatitude());
				location2.setLongitude(pharmacy.getPharmacyLongitude());
				double distance = 0;
				distance = distanceKM(location1.getLatitude(),
						location1.getLongitude(), location2.getLatitude(),
						location2.getLongitude());
				pharmacy.setDistance(distance);
				MarkerOptions venueMarkerOptions = new MarkerOptions()
						.title(pharmacy.getPharmacyName()+", "+pharmacy.getDistance()+" km");
				venueMarkerOptions
						.position(new LatLng(pharmacy.getPharmacyLatitude(),
								pharmacy.getPharmacyLongitude()));
				map.addMarker(venueMarkerOptions)
						.setIcon(
								BitmapDescriptorFactory
										.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

			}
			}
			else
			{
				Toast.makeText(PharmaciesMap.this, "No pharmacies found. Check if your internet connection and your GPS are on!", Toast.LENGTH_LONG).show();
			}

		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			dialog.show();
		}

		@Override
		protected void onProgressUpdate(Void... values) {
		}

		protected String httpRequest(String url) {

			String line = "";
			String responseResult = "";

			HttpClient client = new DefaultHttpClient();
			HttpGet get = new HttpGet(url);

			try {

				HttpResponse response = client.execute(get);

				StatusLine statusLine = response.getStatusLine();
				int statusCode = statusLine.getStatusCode();

				if (statusCode == 200) {
					BufferedReader rd = new BufferedReader(
							new InputStreamReader(response.getEntity()
									.getContent()));
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

		public double distanceKM(double lat1, double lon1, double lat2,
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
	}

}
