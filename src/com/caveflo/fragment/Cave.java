package com.caveflo.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.caveflo.R;
import com.caveflo.dao.Beer;
import com.caveflo.misc.Factory;

public class Cave extends Fragment {

	private TableLayout containerTable;
	private EditText textFilter;
	private TextView countBeer;
	private Spinner spinnerFilter, spinnerTypeFilter;
	private TableRow headerRow;
	private String[] headers;
	private String[] filterBeerName, filterBeerTypes;
	private List<BiereTableRow> beerTableRows;

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
		filterBeerName = new String[] { getString(R.string.filter_all), getString(R.string.filter_drunk), getString(R.string.filter_todrink) };
		filterBeerTypes = new String[] { getString(R.string.filter_all) };

		spinnerTypeFilter = (Spinner) getActivity().findViewById(R.id.filtertype);
		spinnerFilter = (Spinner) getActivity().findViewById(R.id.filterdrunkbeer);
		OnItemSelectedListener filterListener = new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				filterList();
			}

			public void onNothingSelected(AdapterView<?> arg0) {
			}
		};
		spinnerFilter.setOnItemSelectedListener(filterListener);
		spinnerTypeFilter.setOnItemSelectedListener(filterListener);

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

		// Create all beer row
		initList();
	}

	public void initList() {
		beerTableRows.clear();
		for (Beer biere : Factory.get().getBeerReferential().getBeers()) {
			beerTableRows.add(new BiereTableRow(getActivity(), biere));
		}
		initTypeFilter();
		filterList();
		showCount();
	}

	public void initTypeFilter() {
		List<String> beerTypesList = Factory.get().getBeerReferential().getBeerTypes();
		String[] filterBeerTypes = new String[beerTypesList.size() + 1];
		filterBeerTypes[0] = getString(R.string.filter_all);
		int i = 1;
		for (String type : beerTypesList) {
			filterBeerTypes[i++] = type;
		}
		ArrayAdapter<String> adapterBeerType = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, filterBeerTypes);
		spinnerTypeFilter.setAdapter(adapterBeerType);
	}

	public void filterList() {
		String nameFilter = textFilter.getText().toString();
		containerTable.removeAllViews();
		containerTable.addView(headerRow);
		for (BiereTableRow beerTableRow : beerTableRows) {
			// Filtering on name
			if (nameFilter == null || nameFilter.trim().length() == 0 || beerTableRow.getBiere().getName().toLowerCase(Locale.getDefault()).contains(nameFilter.toLowerCase(Locale.getDefault()))) {
				String selectedFilterName = spinnerFilter.getSelectedItem().toString();
				String selectedFilterType = spinnerTypeFilter.getSelectedItem().toString();
				// Filtering on drunk
				if (filterBeerName[0].equals(selectedFilterName) || (filterBeerName[1].equals(selectedFilterName) && beerTableRow.getBiere().isDrunk()) || (filterBeerName[2].equals(selectedFilterName) && !beerTableRow.getBiere().isDrunk())) {
					// Filtering on type
					if (filterBeerTypes[0].equals(selectedFilterType) || selectedFilterType.equals(beerTableRow.getBiere().getType())) {
						containerTable.addView(beerTableRow);
					}
				}
			}
		}
	}

	public void showCount() {
		int count = 0;
		for (BiereTableRow beerTableRow : beerTableRows) {
			if (beerTableRow.getBiere().isDrunk()) {
				count++;
			}
		}
		countBeer.setText(getString(R.string.count_drunk) + count + "/" + beerTableRows.size());
	}

}
