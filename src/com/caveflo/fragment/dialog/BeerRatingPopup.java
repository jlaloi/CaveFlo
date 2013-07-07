package com.caveflo.fragment.dialog;

import java.io.Serializable;
import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.caveflo.R;
import com.caveflo.fragment.BiereTableRow;
import com.caveflo.misc.Factory;

public class BeerRatingPopup extends DialogFragment implements Serializable {

	private static final long serialVersionUID = 6152777249551973750L;
	public static final String dateSep = "/";
	public static final String biereTableRowKey = "BiereTableRowKey";

	private BiereTableRow biereTableRow;
	private TextView textProgress, textDate;
	private EditText comment;
	private SeekBar ratingBar;
	private int year, month, day;

	public static BeerRatingPopup newInstance(BiereTableRow biereTableRow) {
		BeerRatingPopup dialog = new BeerRatingPopup();
		Bundle args = new Bundle();
		args.putSerializable(biereTableRowKey, biereTableRow);
		dialog.setArguments(args);
		return dialog;
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.rating, container, false);
		biereTableRow = (BiereTableRow) getArguments().getSerializable(biereTableRowKey);
		getDialog().setTitle(biereTableRow.getBeer().getName());

		textDate = (TextView) v.findViewById(R.id.ratingtextdate);
		ratingBar = (SeekBar) v.findViewById(R.id.ratingbar);
		comment = (EditText) v.findViewById(R.id.ratingcommentaire);
		if (biereTableRow.getBeer().isDrunk()) {
			ratingBar.setProgress(biereTableRow.getBeer().getRating());
			String[] split = biereTableRow.getBeer().getRatingDate().split(dateSep);
			year = Integer.valueOf(split[2]);
			month = Integer.valueOf(split[1]);
			day = Integer.valueOf(split[0]);
			comment.setText(biereTableRow.getBeer().getComment());
		} else {
			final Calendar c = Calendar.getInstance();
			year = c.get(Calendar.YEAR);
			month = c.get(Calendar.MONTH);
			day = c.get(Calendar.DAY_OF_MONTH);
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
				biereTableRow.getBeer().setRatingDate(textDate.getText().toString());
				biereTableRow.getBeer().setRating(ratingBar.getProgress());
				biereTableRow.getBeer().setComment(comment.getText().toString());
				Factory.get().getBeerReferential().saveRating(biereTableRow.getBeer());
				biereTableRow.onBeerUpdate();
				Factory.get().getFragmentCave().showCount();
				dismiss();
			}
		});

		Button buttonClear = (Button) v.findViewById(R.id.buttonRatingClear);
		buttonClear.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				biereTableRow.getBeer().setRatingDate("");
				biereTableRow.getBeer().setComment("");
				Factory.get().getBeerReferential().saveRating(biereTableRow.getBeer());
				biereTableRow.onBeerUpdate();
				Factory.get().getFragmentCave().showCount();
				dismiss();
			}
		});

		Button buttonCancel = (Button) v.findViewById(R.id.buttonRatingKo);
		buttonCancel.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dismiss();
			}
		});

		Button changeDate = (Button) v.findViewById(R.id.ratingbuttondate);
		changeDate.setOnClickListener(new CreateDatePickerListner(this));

		setDate(year, month, day);
		updateRating();

		return v;
	}

	private void updateRating() {
		textProgress.setText(getString(R.string.rate_string) + ratingBar.getProgress());
	}

	public void setDate(int year, int month, int day) {
		this.year = year;
		this.month = month;
		this.day = day;
		textDate.setText((day > 9 ? day : "0" + day) + dateSep + (month > 9 ? month : "0" + month) + dateSep + year);
	}

	public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

		public static final String beerRatingPopupArg = "BeerRatingPopupArg";
		private BeerRatingPopup brp;

		public Dialog onCreateDialog(Bundle savedInstanceState) {
			brp = (BeerRatingPopup) getArguments().getSerializable(beerRatingPopupArg);
			return new DatePickerDialog(getActivity(), this, brp.getYear(), brp.getMonth(), brp.getDay());
		}

		public void onDateSet(DatePicker view, int year, int month, int day) {
			brp.setDate(year, month, day);
		}
	}

	class CreateDatePickerListner implements OnClickListener {

		private BeerRatingPopup brp;

		public CreateDatePickerListner(BeerRatingPopup brp) {
			super();
			this.brp = brp;
		}

		public void onClick(View arg0) {
			DialogFragment newFragment = new DatePickerFragment();
			Bundle args = new Bundle();
			args.putSerializable(DatePickerFragment.beerRatingPopupArg, brp);
			newFragment.setArguments(args);
			newFragment.show(getFragmentManager(), "datePicker");
		}

	}

	public int getYear() {
		return year;
	}

	public int getMonth() {
		return month;
	}

	public int getDay() {
		return day;
	}

}
