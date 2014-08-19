package mk.finki.ukim.jmm.pharmacylocator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class PickLocation extends FragmentActivity{
	private GoogleMap map;
	MarkerOptions venueMarkerOptions;
	GPSTracker gps;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pick_place_map);
		MapsInitializer.initialize(getApplicationContext());
		
		switch (GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext())) {
        
        case ConnectionResult.SUCCESS:
                
                map = ((SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map_view)).getMap();
      
                        map.setMyLocationEnabled(true);
                        map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                        venueMarkerOptions = new MarkerOptions();  
                        GPSTracker gps=new GPSTracker(getApplicationContext());
                        if(gps.canGetLocation)
                        {
                        	venueMarkerOptions.position(new LatLng(gps.getLatitude(), gps.getLongitude()));
                     		map.addMarker(venueMarkerOptions).setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));;
                     		map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(gps.getLatitude(), gps.getLongitude()), 19));	
                        }
                        else
                        {
                        	venueMarkerOptions.position(new LatLng(0, 0));
                     		map.addMarker(venueMarkerOptions).setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));;
                     		map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(gps.getLatitude(), gps.getLongitude()), 19));
                        	
                        }
                      	 
                      
                        map.setOnMapClickListener(new OnMapClickListener() {
							
							@Override
							public void onMapClick(LatLng arg0) {
								venueMarkerOptions.position(new LatLng(arg0.latitude, arg0.longitude));
	                     		map.addMarker(venueMarkerOptions).setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));;
	                     		map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(arg0.latitude, arg0.longitude), 19));	
	                      
								Intent intent=new Intent(); 
			                    intent.putExtra("LATITUDE", arg0.latitude);
			                    intent.putExtra("LONGITUDE", arg0.longitude);
			                    setResult(2,intent);
			                    finish();//finishing activity  
								
								
							}
						});
                        
                break;
               
        case ConnectionResult.SERVICE_MISSING:
                Toast.makeText(getApplicationContext(), "SERVICE MISSING", Toast.LENGTH_SHORT).show();
                break;
               
        case ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED:
                Toast.makeText(getApplicationContext(), "UPDATE REQUIRED", Toast.LENGTH_SHORT).show();
                break;
               
        default:
                Log.e("MapViewFragment", "Google service paly aviable code" + GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext()));
	}
		
		
	
		
		
		
	}
	
	
	
	
	
	

}
