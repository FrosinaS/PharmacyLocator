package mk.finki.ukim.jmm.pharmacylocator;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
public class DrugsList extends Fragment{
public ProgressDialog dialog;
	public static DrugArrayAdapter drugsAdapter;
	public static List<Drug> drugsList;
	GetData a;
	public static Drug selectedItem;
	//ListView drug_list_view;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(savedInstanceState == null)
		{
			a=new GetData(getActivity());
			drugsList=new ArrayList<Drug>();
			dialog = new ProgressDialog(getActivity());
			dialog.setCancelable(false);
			dialog.setMessage("Searching for drugs...");
			dialog.isIndeterminate();
			a.execute("");
		
		}
	   
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
			 View view =  inflater.inflate(R.layout.drugs_activity, container, false);
			 ListView drug_list_view=(ListView) view.findViewById(R.id.drugs_list_view);
			 drugsAdapter=new  DrugArrayAdapter(this.getActivity(),R.layout.drug_list_item, drugsList);
			 drug_list_view.setAdapter(drugsAdapter);
			 drug_list_view.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					selectedItem=drugsAdapter.drugs.get(position);
					FragmentManager fragmentManager = getFragmentManager();
					FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
					fragmentTransaction.replace(R.id.drugs_activity_layout, new DrugItemInfo(drugsAdapter.drugs.get(position))).addToBackStack(null);
					fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
					fragmentTransaction.commit();
					
					
				}
				 
			});
			 
			 EditText search_drug=(EditText)view.findViewById(R.id.search_drug_edit_text);
			 search_drug.addTextChangedListener(new TextWatcher() {
				
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
				
					
				}
				
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count,
						int after) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void afterTextChanged(Editable s) {
					// TODO Auto-generated method stub
					DrugsList.drugsAdapter.getFilter().filter(s.toString());
				}
			});
			 
			 return view;

	}
	public class GetData extends AsyncTask<String, Void, Cursor> {
	    Context context;
	    public GetData(Context con)
	    {
	    	context=con;
	    }
		@Override
	    protected Cursor doInBackground(String... params) {
			 String URL = "content://mk.finki.ukim.jmm.provider.pharmacy/drugs";
			    Uri drugs = Uri.parse(URL);
			    
			    String[] projection = new String[] { DrugsProvider.KEY_ID, DrugsProvider.KEY_DRUGNAME,
			    		DrugsProvider.KEY_DRUGLATINNAME, DrugsProvider.KEY_DRUGDOSE, DrugsProvider.KEY_DRUGDETAILS };
			    
			    Cursor c = context.getContentResolver().query(drugs, projection, null, null , null);
			  
			    return c;
			    
	    }

	    @Override
	    protected void onPostExecute(Cursor c) {
	    	if (dialog.isShowing())
	    		dialog.dismiss();
	    	if (c.moveToFirst()) {
	            do{
	            	DrugsList.drugsList.add(new Drug(c.getString(c.getColumnIndex(DrugsProvider.KEY_ID)),
	            	c.getString(c.getColumnIndex(DrugsProvider.KEY_DRUGNAME)),
	            	c.getString(c.getColumnIndex(DrugsProvider.KEY_DRUGLATINNAME)),
	            	c.getString(c.getColumnIndex(DrugsProvider.KEY_DRUGDOSE)),
	            	c.getString(c.getColumnIndex(DrugsProvider.KEY_DRUGDETAILS))));
	            	 DrugsList.drugsAdapter.notifyDataSetChanged();
	            } while (c.moveToNext());
	         }
	    	
		    c.close();
		  
	    	
	    	
	    }

	    @Override
	    protected void onPreExecute() {
	    	
	    	dialog.show();
	    }

	    @Override
	    protected void onProgressUpdate(Void... values) {
	    }
	}
	
	
		
		
}


	
	


