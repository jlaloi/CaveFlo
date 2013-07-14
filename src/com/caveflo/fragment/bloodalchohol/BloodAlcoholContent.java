package com.caveflo.fragment.bloodalchohol;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
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
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.caveflo.R;
import com.caveflo.dao.Beer;
import com.caveflo.dao.Drink;
import com.caveflo.misc.Factory;

public class BloodAlcoholContent extends Fragment {

	private EditText weight;
	private TextView result;
	private RadioGroup sexe;
	private TableLayout drinkTable;
	private Spinner hour;
	private BloodAlcoholContentChart chart;
	public static final float limit = 0.5f, elimination = 0.15f;
	private List<DrinkTableRow> drinkTableRows = new ArrayList<DrinkTableRow>();
	private List<Drink> beerToBeAdded = new ArrayList<Drink>();

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View mainView = inflater.inflate(R.layout.alcoolemie, container, false);
		return mainView;
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		drinkTable = (TableLayout) getActivity().findViewById(R.id.drinkList);
		weight = (EditText) getActivity().findViewById(R.id.alco_poid);
		result = (TextView) getActivity().findViewById(R.id.alco_result);
		sexe = (RadioGroup) getActivity().findViewById(R.id.alco_sexe);
		hour = (Spinner) getActivity().findViewById(R.id.alco_hour);
		chart = (BloodAlcoholContentChart) getActivity().findViewById(R.id.alco_chart);
		((Button) getActivity().findViewById(R.id.alco_button)).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				compute();
			}
		});
		((ImageButton) getActivity().findViewById(R.id.alco_add)).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				addDrink(new Drink());
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
		loadUserValues();
		loadDrinks();

		compute();
	}

	public void onStop() {
		super.onStop();
		saveDrinks();
	}

	public void onPause() {
		super.onPause();
		saveDrinks();
	}

	private void addDrink(Drink drink) {
		DrinkTableRow dtr = new DrinkTableRow(getActivity(), drink);
		drinkTable.addView(dtr);
		drinkTableRows.add(dtr);
	}

	private void compute() {
		List<Drink> drinks = new ArrayList<Drink>();
		for (DrinkTableRow drinkTableRow : drinkTableRows) {
			drinks.add(drinkTableRow.getDrink());
		}
		compute(drinks);
	}

	private void compute(List<Drink> drinks) {
		try {
			int weightValue = Integer.valueOf(weight.getText().toString());
			float sexCoeff = 0;
			if (sexe.getCheckedRadioButtonId() == R.id.alco_sexe_homme) {
				sexCoeff = BloodAlcoholContentCalculator.manCoef;
			} else if (sexe.getCheckedRadioButtonId() == R.id.alco_sexe_femme) {
				sexCoeff = BloodAlcoholContentCalculator.womenCoef;
			}
			BloodAlcoholContentCalculator calc = new BloodAlcoholContentCalculator(sexCoeff, weightValue, drinks, hour.getSelectedItemPosition());
			calc.compute();
			float alco = calc.getResult();
			result.setText(getString(R.string.alco_result, alco));
			saveUserValues();
			saveDrinks();
			if (calc.isRelevant()) {
				chart.setContent(calc.getAxisX(), calc.getAxisY(), calc.getValues());
			} else {
				chart.hideContent();
			}
		} catch (Exception e) {
			Log.e("Alcoolemie", "COMPUTE", e);
			result.setText(getString(R.string.alco_error));
		}
	}

	public void removeTableRow(View view) {
		drinkTable.removeView(view);
		drinkTableRows.remove(view);
	}

	private void saveUserValues() {
		SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putString(getString(R.string.saved_weight), weight.getText().toString());
		editor.putInt(getString(R.string.saved_sexe), sexe.getCheckedRadioButtonId());
		editor.putInt(getString(R.string.saved_hour), hour.getSelectedItemPosition());
		editor.commit();
	}

	private void loadUserValues() {
		SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
		String savedWeigth = sharedPref.getString(getString(R.string.saved_weight), "");
		int savedSexe = sharedPref.getInt(getString(R.string.saved_sexe), R.id.alco_sexe_homme);
		int savedHour = sharedPref.getInt(getString(R.string.saved_hour), 25);
		weight.setText(savedWeigth);
		sexe.check(savedSexe);
		if (savedHour == 25) {
			hour.setSelection(Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
		} else {
			hour.setSelection(savedHour);
		}
	}

	private void loadDrinks() {
		drinkTableRows.clear();
		List<Drink> drinks = Factory.get().getBeerDataSource().getDrinks();
		for (Drink drink : drinks) {
			addDrink(drink);
		}
		for (Drink drink : beerToBeAdded) {
			addDrink(drink);
		}
		beerToBeAdded.clear();
	}

	private void saveDrinks() {
		List<Drink> drinks = new ArrayList<Drink>();
		for (DrinkTableRow drinkTableRow : drinkTableRows) {
			drinks.add(drinkTableRow.getDrink());
		}
		Factory.get().getBeerDataSource().clearDrinks();
		Factory.get().getBeerDataSource().saveDrinks(drinks);
		Log.i("Alcoolemie", "Drink saved: " + drinks.size());
	}

	public void addBeer(Beer beer) {
		Drink drink = new Drink();
		drink.setNote(beer.getName());
		drink.setDegree(beer.getDegree());
		beerToBeAdded.add(drink);
	}
}
