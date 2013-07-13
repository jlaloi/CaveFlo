package com.caveflo.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.caveflo.R;

public class Alcoolemie extends Fragment {

	private EditText weight;
	private TextView result;
	private RadioGroup sexe;
	private TableLayout drinkTable;
	public static final float limit = 0.5f, elimination = 0.15f;
	private List<DrinkTableRow> drinks;

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View mainView = inflater.inflate(R.layout.alcoolemie, container, false);
		return mainView;
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		drinks = new ArrayList<DrinkTableRow>();
		drinkTable = (TableLayout) getActivity().findViewById(R.id.drinkList);
		weight = (EditText) getActivity().findViewById(R.id.alco_poid);
		result = (TextView) getActivity().findViewById(R.id.alco_result);
		sexe = (RadioGroup) getActivity().findViewById(R.id.alco_sexe);
		((Button) getActivity().findViewById(R.id.alco_button)).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				compute();
			}
		});
		((ImageButton) getActivity().findViewById(R.id.alco_add)).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				addDrink();
			}
		});

		TableRow headerRow = new TableRow(getActivity());
		for (String header : getResources().getStringArray(R.array.drink)) {
			TextView text = new TextView(getActivity());
			text.setText(header);
			text.setGravity(Gravity.CENTER);
			text.setTypeface(null, Typeface.BOLD);
			text.setPadding(20, 20, 0, 0);
			headerRow.addView(text);
		}
		drinkTable.addView(headerRow);
		addDrink();
	}

	private void addDrink() {
		DrinkTableRow dtr = new DrinkTableRow(getActivity());
		drinkTable.addView(dtr);
		drinks.add(dtr);
	}

	private void compute() {
		try {
			float quantity = 0f;
			for (DrinkTableRow drink : drinks) {
				quantity += drink.getDegree() * drink.getQuantity();
			}
			float weightValue = Float.valueOf(weight.getText().toString());
			float coeff = 0;
			if (sexe.getCheckedRadioButtonId() == R.id.alco_sexe_homme) {
				coeff = 0.7f;
			} else if (sexe.getCheckedRadioButtonId() == R.id.alco_sexe_femme) {
				coeff = 0.6f;
			}
			float alco = computeAlco(quantity, weightValue, coeff);
			if (alco < limit) {
				result.setText(getString(R.string.alco_result_ok, alco));
			} else {
				int h = Math.round(((alco - limit) / elimination));
				result.setText(getString(R.string.alco_result_ko, alco, h));
			}
		} catch (Exception e) {
			Log.e("Alcoolemie", "COMPUTE", e);
			result.setText(getString(R.string.alco_error));
		}
	}

	private float computeAlco(float quantityMl, float weight, float coeff) {
		return Math.round(((quantityMl * 0.8f) / (coeff * weight)) * 100f) / 100f;
	}

	public void removeTableRow(View view) {
		drinkTable.removeView(view);
		drinks.remove(view);
	}
}
