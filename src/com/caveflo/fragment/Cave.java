package com.caveflo.fragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.caveflo.R;
import com.caveflo.cave.Biere;
import com.caveflo.cave.CaveDB;
import com.caveflo.misc.Factory;

public class Cave extends Fragment {

	private TableLayout containerTable;
	private EditText filter;
	private String[] headers;

	public static final String layout = "cave";
	private CaveDB caveDB;

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(getResources().getIdentifier(layout, "layout", container.getContext().getPackageName()), container, false);
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		caveDB = Factory.get().getCaveDB();
		containerTable = (TableLayout) getActivity().findViewById(R.id.containerTable);
		headers = getResources().getStringArray(R.array.biere);

		filter = (EditText) getActivity().findViewById(R.id.filterBeer);
		filter.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(filter.getText().toString().trim().length() == 0 || filter.getText().toString().trim().length() > 1){
					filterList(filter.getText().toString());
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
				filter.setText("");
				filterList(null);
			}
		});
		filterList(null);
	}

	public void filterList(String value) {
		containerTable.removeAllViews();
		TableRow headerRow = new TableRow(getActivity());
		containerTable.addView(headerRow, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		containerTable.setBackgroundColor(getResources().getColor(R.color.grey));
		headerRow.setLayoutParams(new LayoutParams(headers.length));
		int i = 0;
		for (String header : headers) {
			TextView text = createTextView(header, Gravity.CENTER, false, i == headers.length - 1);
			text.setGravity(Gravity.CENTER);
			headerRow.addView(text, i++);
		}
		caveDB.readDb();
		i = 0;
		for (Biere biere : caveDB.readDb()) {
			if (value == null || value.trim().length() == 0 || biere.getName().toLowerCase().contains(value.toLowerCase())) {
				TableRow tableRow = new TableRow(getActivity());
				containerTable.addView(tableRow, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
				i = 0;
				tableRow.addView(createTextView(biere.getName(), Gravity.LEFT, false, false), i++);
				tableRow.addView(createTextView(biere.getDegree() + "", Gravity.CENTER, false, false), i++);
				tableRow.addView(createTextView(biere.getDrunk() + "", Gravity.CENTER, false, false), i++);
				setDrunk(tableRow, biere);
				tableRow.setOnClickListener(new BiereListener(tableRow, biere));
			}
		}
	}

	class BiereListener implements OnClickListener {

		private Biere biere;
		private TableRow tableRow;

		public BiereListener(TableRow tableRow, Biere biere) {
			super();
			this.biere = biere;
			this.tableRow = tableRow;
		}

		public void onClick(View v) {
			if (biere.getDrunk() == null || biere.getDrunk().trim().length() == 0) {
				biere.setDrunk(new SimpleDateFormat("dd/MM/yy").format(Calendar.getInstance().getTime()));
			} else {
				biere.setDrunk("");
			}
			caveDB.saveDrunk(biere);
			setDrunk(tableRow, biere);
			Toast.makeText(getActivity(), biere.getName(), Toast.LENGTH_SHORT).show();
		}
	}

	private TableRow setDrunk(TableRow tableRow, Biere biere) {
		if (biere.getDrunk() == null || biere.getDrunk().trim().length() == 0) {
			tableRow.setBackgroundColor(getResources().getColor(R.color.white));
		} else {
			tableRow.setBackgroundColor(getResources().getColor(R.color.drunk));
		}
		tableRow.removeViewAt(2);
		tableRow.addView(createTextView(biere.getDrunk(), Gravity.CENTER, false, true), 2);
		return tableRow;
	}

	private TextView createTextView(String value, int gravity, boolean endline, boolean endcolumn) {
		TextView text = new TextView(getActivity(), null, R.style.frag3HeaderCol);
		int bottom = endline ? 1 : 0;
		int right = endcolumn ? 1 : 0;
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 0.3f);
		params.setMargins(1, 1, right, bottom);
		text.setLayoutParams(params);
		text.setPadding(4, 4, 10, 4);
		text.setText(value);
		return text;
	}

}
