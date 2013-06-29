package com.caveflo.fragment;

import java.util.Calendar;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.caveflo.R;

public class BeerRatingPopup extends DialogFragment {

	private BiereTableRow biereTableRow;
	private TextView textProgress;
	private SeekBar ratingBar;

	public BeerRatingPopup(BiereTableRow biereTableRow) {
		super();
		this.biereTableRow = biereTableRow;
	}

	public static BeerRatingPopup newInstance(BiereTableRow biereTableRow) {
		BeerRatingPopup dialog = new BeerRatingPopup(biereTableRow);
		dialog.setArguments(new Bundle());
		return dialog;
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.rating, container, false);
		getDialog().setTitle(biereTableRow.getBiere().getName());

		ratingBar = (SeekBar) v.findViewById(R.id.ratingbar);
		if (biereTableRow.getBiere().isDrunk()) {
			ratingBar.setProgress(biereTableRow.getBiere().getRating());
		}
		ratingBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			public void onStopTrackingTouch(SeekBar seekBar) {
			}

			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				updateRating();
			}
		});
		textProgress = (TextView) v.findViewById(R.id.ratingvalue);

		Button buttonOk = (Button) v.findViewById(R.id.buttonRatingOk);
		buttonOk.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				biereTableRow.getBiere().setDrunk(Calendar.getInstance().getTime());
				biereTableRow.getBiere().setRating(ratingBar.getProgress());
				biereTableRow.updateDrunk();
				dismiss();
			}
		});
		Button buttonClear = (Button) v.findViewById(R.id.buttonRatingClear);
		buttonClear.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				biereTableRow.getBiere().setDrunk("");
				biereTableRow.updateDrunk();
				dismiss();
			}
		});
		Button buttonCancel = (Button) v.findViewById(R.id.buttonRatingKo);
		buttonCancel.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dismiss();
			}
		});

		updateRating();
		return v;
	}

	private void updateRating() {
		textProgress.setText(getString(R.string.rate_string) + ratingBar.getProgress());
	}

}
