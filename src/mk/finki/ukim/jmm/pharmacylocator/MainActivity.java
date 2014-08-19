package mk.finki.ukim.jmm.pharmacylocator;


import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


public class MainActivity extends ActionBarActivity {
	

	Button listAllPharmacies, searchNearest, searchPharmacy, drugsList, aboutAuthor, pharmaciesMap;
	AdView adView;
    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        if(savedInstanceState == null){
           
            }
         
        
        
        searchPharmacy=(Button)findViewById(R.id.searchPharmacy);
        searchPharmacy.setOnClickListener(searchPharmacyOnClick);
        
        searchNearest=(Button)findViewById(R.id.searchNearby);
        searchNearest.setOnClickListener(searchNearestOnClick);
        
        drugsList=(Button)findViewById(R.id.drugsButton);
        drugsList.setOnClickListener(drugsListOnClick);
       
        pharmaciesMap=(Button)findViewById(R.id.pharmaciesMap);
        pharmaciesMap.setOnClickListener(pharmaciesMapOnClick);
           
    }
    
    private OnClickListener pharmaciesMapOnClick=new OnClickListener(){

		@Override
		public void onClick(View v) {
			Intent p = new Intent(getApplicationContext(),
					PharmaciesMap.class);
			startActivity(p);
			
		}
    	
    	
    };
    
    
    
    
    private OnClickListener searchPharmacyOnClick=new OnClickListener(){

		@Override
		public void onClick(View v) {
			Intent globalView=new Intent(MainActivity.this, GlobalView.class);
			globalView.putExtra("FRAGMENT_TYPE", "search pharmacy");
			startActivity(globalView);
			
		}
    	
    	
    };
    
    private OnClickListener searchNearestOnClick=new OnClickListener(){

		@Override
		public void onClick(View v) {
			Intent globalView=new Intent(getApplicationContext(), GlobalView.class);
			globalView.putExtra("FRAGMENT_TYPE", "search nearest");
			startActivity(globalView);
			
		}
    	
    	
    };
  
    
    private OnClickListener drugsListOnClick=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Intent listDrugView=new Intent(getApplicationContext(), DrugActivityLayout.class);
			startActivity(listDrugView);
		}
	};
	
	
	public void onConfigurationChanged(Configuration newConfig) 

	{

	  setContentView(R.layout.activity_main);

	  super.onConfigurationChanged(newConfig);
	  
	  
      searchPharmacy=(Button)findViewById(R.id.searchPharmacy);
      searchPharmacy.setOnClickListener(searchPharmacyOnClick);
      
      searchNearest=(Button)findViewById(R.id.searchNearby);
      searchNearest.setOnClickListener(searchNearestOnClick);
      
      drugsList=(Button)findViewById(R.id.drugsButton);
      drugsList.setOnClickListener(drugsListOnClick);
      
      pharmaciesMap=(Button)findViewById(R.id.pharmaciesMap);
      pharmaciesMap.setOnClickListener(pharmaciesMapOnClick);
	}
}





