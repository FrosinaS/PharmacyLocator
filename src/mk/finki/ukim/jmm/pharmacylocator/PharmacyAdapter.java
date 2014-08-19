package mk.finki.ukim.jmm.pharmacylocator;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

public class PharmacyAdapter extends ArrayAdapter<Pharmacy> implements Filterable{
	
	Context context;
	 List<Pharmacy> pharmacies;
	int layoutResourceId;
	private NearbyFilter mFilter = new NearbyFilter();
	private ItemFilter mFilter1 = new ItemFilter();
	public PharmacyAdapter(Context context, int layoutResourceId, List<Pharmacy> pharmacies)
	{
		super(context, layoutResourceId, pharmacies);
		this.context=context;
		this.pharmacies=pharmacies;
		this.layoutResourceId=layoutResourceId;
		
		
	}
	public Filter getFilter1() {
		return mFilter1;
	}
	
	public Filter getFilter() {
		return mFilter;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(layoutResourceId, parent, false);
		
		TextView pharmacyName=(TextView) rowView.findViewById(R.id.pharmacy_name);
		TextView pharmacyAdress=(TextView) rowView.findViewById(R.id.pharmacy_adress);
		TextView pharmacyKm=(TextView) rowView.findViewById(R.id.km_away);
		
		
		pharmacyName.setText(pharmacies.get(position).getPharmacyName());
		pharmacyAdress.setText(pharmacies.get(position).getPharmacyAdress());
		pharmacyKm.setText(Double.toString((pharmacies.get(position).getDistance()))+" km");
		return rowView;
	}
	
	public int getCount () {
	    return pharmacies.size ();
	}
	
	
	private class NearbyFilter extends Filter {
		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			
			
			FilterResults results = new FilterResults();
			final List<Pharmacy> list = new ArrayList<Pharmacy>();
			list.addAll(GlobalView.pharmaciesList);
			
			final ArrayList<Pharmacy> nlist = new ArrayList<Pharmacy>();
			Location loc=new Location("A");
			loc.setLatitude(GlobalView.latitude);
			loc.setLongitude(GlobalView.longitude);
			Location loc2=new Location("B");
			
			double distance=0.0;
			for (Pharmacy ph : list) {
				loc2.setLongitude(ph.getPharmacyLongitude());
				loc2.setLatitude(ph.getPharmacyLatitude());
				distance=GlobalView.distanceKM(loc.getLatitude(), loc.getLongitude(), loc2.getLatitude(), loc2.getLongitude());
				if ((distance < 3.0 )  && (distance > 0.0 )) {
					ph.setDistance(distance);
						nlist.add(ph);
					}
				}
			results.values = nlist;
			results.count = nlist.size();
 
			return results;
		}
		
		@SuppressWarnings("unchecked")
		@Override
		protected void publishResults(CharSequence constraint, FilterResults results) {
			if(results.count == 0)
			{notifyDataSetInvalidated();}
			else{
			pharmacies = (ArrayList<Pharmacy>) results.values;
			notifyDataSetChanged();
			}
			
		}
 
	}
	
	private class ItemFilter extends Filter {
		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			
			String filterString = constraint.toString().toLowerCase();
			
			FilterResults results = new FilterResults();
			
			final List<Pharmacy> list = new ArrayList<Pharmacy>();
			list.addAll(GlobalView.pharmaciesList);
 
			int count = list.size();
			final ArrayList<Pharmacy> nlist = new ArrayList<Pharmacy>();
 
			String filterableString ;
			
			for (int i = 0; i < count; i++) {
				filterableString = list.get(i).getPharmacyName();
					if (filterableString.toLowerCase().contains(filterString)) {
						nlist.add(list.get(i));
					
				}
			}
			
			results.values = nlist;
			results.count = nlist.size();
 
			return results;
		}
		
		@SuppressWarnings("unchecked")
		@Override
		protected void publishResults(CharSequence constraint, FilterResults results) {
			if(results.count == 0)
			{notifyDataSetInvalidated();}
			else{
			pharmacies = (ArrayList<Pharmacy>) results.values;
			notifyDataSetChanged();
			}
		}
 
	}
	
	

}
