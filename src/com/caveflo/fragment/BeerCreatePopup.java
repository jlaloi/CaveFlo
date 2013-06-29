package com.caveflo.fragment;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.caveflo.R;
import com.caveflo.misc.Factory;

public class BeerCreatePopup extends DialogFragment {

	private EditText beerName;
	private TextView degreeProgress;
	private SeekBar degreeBar;

	public static BeerCreatePopup newInstance() {
		BeerCreatePopup dialog = new BeerCreatePopup();
		dialog.setArguments(new Bundle());
		return dialog;
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.addbeer, container, false);
		getDialog().setTitle(getString(R.string.create_title));
		
		degreeBar = (SeekBar) v.findViewById(R.id.createbeerdegree);
		degreeBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			public void onStopTrackingTouch(SeekBar seekBar) {
			}

			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				updateDegree();
			}

		});
		degreeProgress = (TextView) v.findViewById(R.id.degreevalue);
		
		beerName = (EditText) v.findViewById(R.id.createbeername);

		Button buttonOk = (Button) v.findViewById(R.id.buttonCreateOk);
		buttonOk.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Factory.get().getCaveDB().addUserBeer(beerName.getText().toString(), getDegree());
				dismiss();
			}
		});

		Button buttonCancel = (Button) v.findViewById(R.id.buttonCreateKo);
		buttonCancel.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dismiss();
			}
		});

		updateDegree();
		return v;
	}
	
	private float getDegree(){
		return degreeBar.getProgress() / 10f;
	}
	
	private void updateDegree() {
		degreeProgress.setText(getString(R.string.degree_string) + getDegree());
	}

}
