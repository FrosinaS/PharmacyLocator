package mk.finki.ukim.jmm.pharmacylocator;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class SearchNearest extends Fragment{

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
	
		super.onCreate(savedInstanceState);
		
		
		
	}
	View view;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		 view =  inflater.inflate(R.layout.search_nearest, container, false);
		 Button find_location=(Button) view.findViewById(R.id.find_location);
		 find_location.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent startActivity=new Intent(view.getContext(), PickLocation.class);
				startActivityForResult(startActivity, 2);
			}					
			});
		
		 
		 
		 
		
		 return view;
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)  
    {  
              super.onActivityResult(requestCode, resultCode, data);  
                  
               // check if the request code is same as what is passed  here it is 2  
                if(requestCode==2)  
                      {  
                         
                         double latitude=data.getDoubleExtra("LATITUDE", 0.0);
                         double longitude=data.getDoubleExtra("LONGITUDE", 0.0);
                 		double distance=0.0;
                		GlobalView.location1.setLongitude(longitude);
                		GlobalView.location1.setLatitude(latitude);
                		//GlobalView.getPh(getActivity());
                		getActivity().startService(new Intent(getActivity(), SearchPharmaciesService.class));
                         //GlobalView.pharmacyAdapter.getFilter().filter(Double.toString(distance));
                         
                       
                      }  
  
  }  
	
	
	

}
