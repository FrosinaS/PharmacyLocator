package mk.finki.ukim.jmm.pharmacylocator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
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

import android.app.ProgressDialog;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class SearchPharmaciesService extends Service {

	@SuppressWarnings("deprecation")
	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
		GetPharmacies task = new GetPharmacies();
		task.execute();
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	String urlA;

	public class GetPharmacies extends
			AsyncTask<Void, Void, ArrayList<Pharmacy>> {

		@Override
		protected ArrayList<Pharmacy> doInBackground(Void... params) {

			urlA = "https://api.foursquare.com/v2/venues/search?ll="
					+ GlobalView.location1.getLatitude()
					+ ","
					+ GlobalView.location1.getLongitude()
					+ "&categoryId=4bf58dd8d48988d10f951735"
					+ "&limit=50&oauth_token=KV3WTKON301SAXW1HNF4BC40WVRARJL2OO5UTV0SDIP5NMPA&v=20140506";
			String response = httpRequest(urlA);
			SearchData dataProcesor = new SearchData();
			return (ArrayList<Pharmacy>) dataProcesor.parseSearchJSON(response);
		}

		@Override
		protected void onPostExecute(ArrayList<Pharmacy> pharmacies) {
			super.onPostExecute(pharmacies);

			if (GlobalView.dialog.isShowing()) {
				GlobalView.dialog.dismiss();
				// GlobalView.dialog = null;

			}
			if(pharmacies != null){
				GlobalView.visible.setVisibility(View.INVISIBLE);
			GlobalView.pharmaciesList.clear();
			GlobalView.pharmaciesList.addAll(pharmacies);
			GlobalView.pharmacyAdapter.notifyDataSetChanged();

				for (Pharmacy pharmacy : GlobalView.pharmaciesList) {

					Location location1 = new Location("Point B");
					location1.setLatitude(pharmacy.getPharmacyLatitude());
					location1.setLongitude(pharmacy.getPharmacyLongitude());
					double distance = 0;
					distance = distanceKM(GlobalView.location1.getLatitude(),
							GlobalView.location1.getLongitude(), location1.getLatitude(),
							location1.getLongitude());
					pharmacy.setDistance(distance);
				}
			
			GlobalView.pharmacyAdapter.sort(new Comparator<Pharmacy>() {
			    @Override
			    public int compare(Pharmacy lhs, Pharmacy rhs) {
			    	Double d=lhs.getDistance();
			        return  d.compareTo(rhs.getDistance());
			    }
			});
			GlobalView.pharmacyAdapter.notifyDataSetChanged();
			}
			else
			{
				Toast.makeText(getApplicationContext(),"Check if your internet connection and your GPS are on!", Toast.LENGTH_LONG).show();
				GlobalView.visible.setVisibility(View.VISIBLE);
			}
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			GlobalView.dialog.show();
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
	}

	public double distanceKM(double lat1, double lon1, double lat2, double lon2) {
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
