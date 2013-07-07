package com.caveflo.fragment.dialog;

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
import com.caveflo.dao.Beer;
import com.caveflo.misc.Factory;

public class CustomBeerPopup extends DialogFragment {

	private EditText beerName, beerCountry, beerType;
	private TextView degreeProgress;
	private SeekBar degreeBar;
	private static final float degreeBarScale = 10f;
	private Beer beer;
	public static final String beerArgument = "BeerArgument";

	public static CustomBeerPopup newInstance(Beer beer) {
		CustomBeerPopup dialog = new CustomBeerPopup();
		Bundle args = new Bundle();
		if (beer != null) {
			args.putSerializable(beerArgument, beer);
		}
		dialog.setArguments(args);
		return dialog;
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.custombeer, container, false);

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
		beerCountry = (EditText) v.findViewById(R.id.createbeercountry);
		beerType = (EditText) v.findViewById(R.id.createbeertype);

		Button buttonOk = (Button) v.findViewById(R.id.buttonCreateOk);
		buttonOk.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (beer != null) {
					beer.setName(beerName.getText().toString());
					beer.setDegree(getDegree());
					beer.setType(beerType.getText().toString());
					beer.setCountry(beerCountry.getText().toString());
					Factory.get().getBeerReferential().updateBeer(beer);
					Factory.get().getFragmentCave().onBeerModification(beer);
				} else {
					Beer createdBeer = Factory.get().getBeerReferential().createCustomBeer(beerName.getText().toString(), getDegree(), beerType.getText().toString(), beerCountry.getText().toString());
					Factory.get().getFragmentCave().onBeerCreation(createdBeer);
				}
				dismiss();
			}
		});

		Button buttonCancel = (Button) v.findViewById(R.id.buttonCreateKo);
		buttonCancel.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dismiss();
			}
		});

		Button buttonDelete = (Button) v.findViewById(R.id.buttonDelete);
		buttonDelete.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (beer != null) {
					Factory.get().getFragmentCave().onBeerDeletion(beer);
					Factory.get().getBeerReferential().deleteBeer(beer);
					dismiss();
				}
			}
		});

		// Modify or create
		if (getArguments().containsKey(beerArgument) && getArguments().getSerializable(beerArgument) != null) {
			getDialog().setTitle(getString(R.string.modify_title));
			beer = (Beer) getArguments().getSerializable(beerArgument);
			beerName.setText(beer.getName());
			beerCountry.setText(beer.getCountry());
			beerType.setText(beer.getType());
			degreeBar.setProgress((int) (beer.getDegree() * degreeBarScale));
		} else {
			getDialog().setTitle(getString(R.string.create_title));
			buttonDelete.setVisibility(View.INVISIBLE);
		}

		updateDegree();
		return v;
	}

	private float getDegree() {
		return degreeBar.getProgress() / degreeBarScale;
	}

	private void updateDegree() {
		degreeProgress.setText(getString(R.string.degree_string) + getDegree());
	}

}
