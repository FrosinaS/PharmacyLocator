package mk.finki.ukim.jmm.pharmacylocator;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

public class DrugArrayAdapter extends ArrayAdapter<Drug> implements Filterable{
	
	Context context;
	 List<Drug> drugs;
	int layoutResourceId;
	private ItemFilter mFilter = new ItemFilter();
	public DrugArrayAdapter(Context context, int layoutResourceId, List<Drug> drugs)
	{
		super(context, layoutResourceId, drugs);
		this.context=context;
		this.drugs=drugs;
		this.layoutResourceId=layoutResourceId;
		
		
	}
	public Filter getFilter() {
		return mFilter;
	}
	
	public int getCount () {
	    return drugs.size ();
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(layoutResourceId, parent, false);
		
		TextView drugName=(TextView) rowView.findViewById(R.id.name_text_view);
		TextView drugLatinName=(TextView) rowView.findViewById(R.id.latin_name_text_view);
		TextView drugId=(TextView) rowView.findViewById(R.id.drug_id);
		
		drugName.setText(drugs.get(position).getDrugName());
		drugLatinName.setText(drugs.get(position).getDrugLatinName());
		drugId.setText(drugs.get(position).getDrugId());
		
		return rowView;
	}
	
	private class ItemFilter extends Filter {
		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			
			String filterString = constraint.toString().toLowerCase();
			
			FilterResults results = new FilterResults();
			
			final List<Drug> list = new ArrayList<Drug>();
			list.addAll(DrugsList.drugsList);
 
			int count = list.size();
			final ArrayList<Drug> nlist = new ArrayList<Drug>(count);
 
			String filterableString ;
			
			for (int i = 0; i < count; i++) {
				filterableString = list.get(i).getDrugLatinName();
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
			drugs=(ArrayList<Drug>) results.values;
			notifyDataSetChanged();
			}
		}
 
	}
	

}
