package mk.finki.ukim.jmm.pharmacylocator;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;



public class SearchData extends Service{


	public Object parseSearchJSON(String response) {

		try {

			JSONObject jsonObj = new JSONObject(response);

			JSONObject data = jsonObj.getJSONObject("response");

			JSONArray groups = data.getJSONArray("venues");

			ArrayList<Pharmacy> pharmacyList = new ArrayList<Pharmacy>();
			for (int i = 0; i < groups.length(); i++)
				pharmacyList.add(JSONItemToPharmacy(groups.getJSONObject(i)));

			return pharmacyList;
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return null;

	}


	public Pharmacy JSONItemToPharmacy(JSONObject JSONPharmacy) throws JSONException {

		Pharmacy pharmacy = new Pharmacy();
		String pharmacyName = "/";
		String pharmacyPhone="/";
		String pharmacyFormattedPhone = "/";
		String pharmacyAddress = "/";
		Double pharmacyLat = -1.0;
		Double pharmacyLng = -1.0;
		String pharmacyCity = "/";
		String pharmacyCountry = "/";

		if(JSONPharmacy.has("categories"))
		{
			JSONArray JSONCategory=(JSONArray)JSONPharmacy.get("categories");
			if(JSONCategory.getJSONObject(0).has("name"))
			{
				if(JSONCategory.getJSONObject(0).get("name").toString().equals("Drugstore / Pharmacy"))
				{
					
					if (JSONPharmacy.has("name"))
						pharmacyName = (String) JSONPharmacy.get("name");
			
					if (JSONPharmacy.has("contact")) {
			
						JSONObject JSONContact = (JSONObject) JSONPharmacy.get("contact");
						if (JSONContact.has("phone"))
							pharmacyPhone = (String) JSONContact.get("phone");
						if (JSONContact.has("formattedPhone"))
							pharmacyFormattedPhone = (String) JSONContact.get("formattedPhone");
					}
			
					if (JSONPharmacy.has("location")) {
			
						JSONObject JSONLocation = (JSONObject) JSONPharmacy.get("location");
						if (JSONLocation.has("address"))
							pharmacyAddress = (String) JSONLocation.get("address");
						if (JSONLocation.has("lat"))
							pharmacyLat = (Double) JSONLocation.get("lat");
						if (JSONLocation.has("lng"))
							pharmacyLng = (Double) JSONLocation.get("lng");
						if (JSONLocation.has("city"))
							pharmacyCity = (String) JSONLocation.get("city");
						if (JSONLocation.has("country"))
							pharmacyCountry = (String) JSONLocation.get("country");
					}
				}
			}
		}

		pharmacy.setPharmacyName(pharmacyName);
		pharmacy.setPharmacyTelephoneNumber(pharmacyPhone);
		pharmacy.setPharmacyFormattedTelephoneNumber(pharmacyFormattedPhone);
		pharmacy.setPharmacyAdress(pharmacyAddress);
		pharmacy.setPharmacyLatitude(pharmacyLat);
		pharmacy.setPharmacyLongitude(pharmacyLng);
		pharmacy.setPharmacyCity(pharmacyCity);
		pharmacy.setPharmacyCountry(pharmacyCountry);
		
		return pharmacy;
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

}
