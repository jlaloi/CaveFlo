package com.caveflo.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.caveflo.R;
import com.caveflo.dao.Beer;
import com.caveflo.misc.Factory;

public class Cave extends Fragment {

	private TableLayout containerTable;
	private EditText textFilter;
	private TextView countBeer;
	private Spinner spinnerDrunkFilter, spinnerTypeFilter, spinnerCountryFilter;
	private TableRow headerRow;
	private String[] headers;
	private String[] filterBeerDrunkValues;
	private List<BiereTableRow> beerTableRows;
	public String allItem;
	public static final int spinnerStyleId = android.R.layout.simple_spinner_item;

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View mainView = inflater.inflate(R.layout.cave, container, false);
		return mainView;
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		beerTableRows = new ArrayList<BiereTableRow>();
		containerTable = (TableLayout) getActivity().findViewById(R.id.containerTable);
		countBeer = (TextView) getActivity().findViewById(R.id.beercount);
		headers = getResources().getStringArray(R.array.biere);
		filterBeerDrunkValues = getResources().getStringArray(R.array.filter_drunk_biere);

		spinnerCountryFilter = (Spinner) getActivity().findViewById(R.id.filtercountry);
		spinnerTypeFilter = (Spinner) getActivity().findViewById(R.id.filtertype);
		spinnerDrunkFilter = (Spinner) getActivity().findViewById(R.id.filterdrunkbeer);
		ArrayAdapter<String> adapterBeerDrunk = new ArrayAdapter<String>(getActivity(), spinnerStyleId, filterBeerDrunkValues);
		spinnerDrunkFilter.setAdapter(adapterBeerDrunk);
		
		allItem = getString(R.string.filter_all);

		OnItemSelectedListener filterListener = new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				filterList();
			}

			public void onNothingSelected(AdapterView<?> arg0) {
			}
		};

		spinnerDrunkFilter.setOnItemSelectedListener(filterListener);
		spinnerTypeFilter.setOnItemSelectedListener(filterListener);
		spinnerCountryFilter.setOnItemSelectedListener(filterListener);

		textFilter = (EditText) getActivity().findViewById(R.id.filterBeerText);
		textFilter.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (textFilter.getText().toString().trim().length() == 0 || textFilter.getText().toString().trim().length() > 1) {
					filterList();
				}
			}

			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			public void afterTextChanged(Editable s) {
			}
		});

		countBeer.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				affToastCount();

			}
		});

		// Create header row
		headerRow = new TableRow(getActivity());
		for (String header : headers) {
			TextView text = new TextView(getActivity());
			text.setText(header);
			text.setGravity(Gravity.CENTER);
			text.setTypeface(null, Typeface.BOLD);
			text.setPadding(4, 4, 0, 0);
			headerRow.addView(text);
		}

		// Create all beer rows
		initList();
	}

	public void initList() {
		beerTableRows.clear();
		List<Beer> beers = Factory.get().getBeerReferential().getBeers();
		Activity activity = getActivity();
		for (Beer beer : beers) {
			beerTableRows.add(new BiereTableRow(activity, beer));
		}
		initTypeFilter();
		initCountryFilter();
		filterList();
		showCount();
	}

	public void onBeerCreation(Beer beer) {
		initList();
	}

	public void onBeerModification(Beer beer) {
		getBiereTableRow(beer).updateBeer(beer);
	}

	public void onBeerDeletion(Beer beer) {
		BiereTableRow toRemove = getBiereTableRow(beer);
		containerTable.removeView(toRemove);
		beerTableRows.remove(toRemove);
	}

	private BiereTableRow getBiereTableRow(Beer beer) {
		BiereTableRow result = null;
		for (BiereTableRow biereTableRow : beerTableRows) {
			if (biereTableRow.getBeer().equals(beer)) {
				result = biereTableRow;
				break;
			}
		}
		return result;
	}

	public void initTypeFilter() {
		List<String> beerTypesList = Factory.get().getBeerReferential().getBeerTypes();
		String[] filterBeerTypes = new String[beerTypesList.size() + 1];
		filterBeerTypes[0] = allItem;
		int i = 1;
		for (String type : beerTypesList) {
			filterBeerTypes[i++] = type;
		}
		ArrayAdapter<String> adapterBeerType = new ArrayAdapter<String>(getActivity(), spinnerStyleId, filterBeerTypes);
		spinnerTypeFilter.setAdapter(adapterBeerType);
	}

	public void initCountryFilter() {
		List<String> beerCountryList = Factory.get().getBeerReferential().getBeerCountries();
		String[] filterBeerCountry = new String[beerCountryList.size() + 1];
		filterBeerCountry[0] = allItem;
		int i = 1;
		for (String type : beerCountryList) {
			filterBeerCountry[i++] = type;
		}
		ArrayAdapter<String> adapterBeerCountry = new ArrayAdapter<String>(getActivity(), spinnerStyleId, filterBeerCountry);
		spinnerCountryFilter.setAdapter(adapterBeerCountry);
	}

	public void filterList() {
		Locale locale = Locale.getDefault();
		String nameFilter = textFilter.getText().toString().toLowerCase(locale);
		String selectedFilterType = spinnerTypeFilter.getSelectedItem().toString();
		String selectedFilterCountry = spinnerCountryFilter.getSelectedItem().toString();

		containerTable.removeAllViews();
		containerTable.addView(headerRow);
		
		for (BiereTableRow beerTableRow : beerTableRows) {

			// Filtering on name
			if (nameFilter == null || nameFilter.trim().length() == 0 || beerTableRow.getBeer().getName().toLowerCase(locale).contains(nameFilter)) {

				// Filtering on drunk
				if (spinnerDrunkFilter.getSelectedItemPosition() == 0 || (spinnerDrunkFilter.getSelectedItemPosition() == 1 && !beerTableRow.getBeer().isDrunk()) || (spinnerDrunkFilter.getSelectedItemPosition() == 2 && beerTableRow.getBeer().isDrunk())) {

					// Filtering on type
					if (spinnerTypeFilter.getSelectedItemPosition() == 0 || selectedFilterType.equals(beerTableRow.getBeer().getType())) {

						// Filtering on country
						if (spinnerCountryFilter.getSelectedItemPosition() == 0 || selectedFilterCountry.equals(beerTableRow.getBeer().getCountry())) {
							containerTable.addView(beerTableRow);
						}

					}

				}

			}
		}
	}

	public void affToastCount() {
		String text = getString(R.string.numberdrunk, Factory.get().getBeerReferential().getBeerDrunkCount(), Factory.get().getBeerReferential().getBeerCount());
		Toast.makeText(getActivity(), text, Toast.LENGTH_LONG).show();
	}

	public void showCount() {
		int pourc = 0;
		if (Factory.get().getBeerReferential().getBeerCount() > 0) {
			pourc = Factory.get().getBeerReferential().getBeerDrunkCount() * 100 / Factory.get().getBeerReferential().getBeerCount();
		}
		countBeer.setText(pourc + "%");
	}

}
