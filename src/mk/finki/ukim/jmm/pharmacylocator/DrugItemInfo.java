package mk.finki.ukim.jmm.pharmacylocator;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DrugItemInfo extends Fragment {

	Drug item;

	public DrugItemInfo(Drug item) {
		this.item = item;
	}

	public DrugItemInfo() {
		item = DrugsList.selectedItem;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.drug_info_layout, container,
				false);

		TextView drugName = (TextView) view
				.findViewById(R.id.drug_name_text_view);
		TextView drugLatinName = (TextView) view
				.findViewById(R.id.latin_drug_text_view);
		TextView drugDose = (TextView) view
				.findViewById(R.id.drug_dose_text_view);
		TextView drugDetails = (TextView) view
				.findViewById(R.id.drug_details_text_view);

		drugName.setText(item.getDrugName());
		drugLatinName.setText(item.getDrugLatinName());
		drugDose.setText(item.getDrugDose());
		drugDetails.setText(item.getDrugDetails());

		return view;

	}

}
