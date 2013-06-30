package com.caveflo.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.caveflo.R;
import com.caveflo.cave.Biere;
import com.caveflo.cave.CaveDB;
import com.caveflo.misc.Factory;

public class Cave extends Fragment {

	private TableLayout containerTable;
	private EditText textFilter;
	private TextView countBeer;
	private Spinner spinnerFilter;
	private TableRow headerRow;
	private String[] headers;
	private String[] filterDrunk;
	private List<BiereTableRow> beerTableRows;

	public static final String layout = "cave";
	private CaveDB caveDB;

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(getResources().getIdentifier(layout, "layout", container.getContext().getPackageName()), container, false);
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		beerTableRows = new ArrayList<BiereTableRow>();
		caveDB = Factory.get().getCaveDB();
		containerTable = (TableLayout) getActivity().findViewById(R.id.containerTable);
		countBeer = (TextView) getActivity().findViewById(R.id.beercount);
		headers = getResources().getStringArray(R.array.biere);
		filterDrunk = new String[] { getString(R.string.filter_all), getString(R.string.filter_drunk), getString(R.string.filter_todrink) };

		spinnerFilter = (Spinner) getActivity().findViewById(R.id.filterdrunkbeer);
		spinnerFilter.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				filterList();
			}

			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

		textFilter = (EditText) getActivity().findViewById(R.id.filterBeer);
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
		Button buttonClear = (Button) getActivity().findViewById(R.id.buttonClear);
		buttonClear.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				textFilter.setText("");
				filterList();
			}
		});

		// Create header row
		headerRow = new TableRow(getActivity());
		for (String header : headers) {
			TextView text = new TextView(getActivity());
			text.setText(header);
			text.setTypeface(null, Typeface.BOLD);
			text.setPadding(4, 4, 0, 0);
			headerRow.addView(text);
		}

		// Create all beer row
		initList();
	}

	public void initList() {
		beerTableRows.clear();
		for (Biere biere : caveDB.readDb()) {
			beerTableRows.add(new BiereTableRow(getActivity(), biere));
		}
		filterList();
		showCount();
	}

	public void filterList() {
		String value = textFilter.getText().toString();
		containerTable.removeAllViews();
		containerTable.addView(headerRow);
		for (BiereTableRow beerTableRow : beerTableRows) {
			if (value == null || value.trim().length() == 0 || beerTableRow.getBiere().getName().toLowerCase().contains(value.toLowerCase())) {
				String selectedFilterValue = spinnerFilter.getSelectedItem().toString();
				if (filterDrunk[0].equals(selectedFilterValue) || (filterDrunk[1].equals(selectedFilterValue) && beerTableRow.getBiere().isDrunk()) || (filterDrunk[2].equals(selectedFilterValue) && !beerTableRow.getBiere().isDrunk())) {
					containerTable.addView(beerTableRow);
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
