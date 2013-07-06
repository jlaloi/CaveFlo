package com.caveflo.fragment;

import java.io.Serializable;

import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.TableRow;
import android.widget.TextView;

import com.caveflo.R;
import com.caveflo.dao.Beer;
import com.caveflo.fragment.dialog.BeerRatingPopup;
import com.caveflo.misc.Factory;

public class BiereTableRow extends TableRow implements Serializable {

	private static final long serialVersionUID = -4037399769129818273L;
	private Beer biere;
	private Context context;
	private TextView rating, ratingDate;

	public BiereTableRow(Context context) {
		super(context);
	}

	public BiereTableRow(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public BiereTableRow(Context context, Beer biere) {
		super(context);
		this.biere = biere;
		this.context = context;

		TextView name = createTextView(biere.getName(), Gravity.LEFT);
		if (biere.isCustom()) {
			name.setTypeface(null, Typeface.BOLD_ITALIC);
		}
		TextView degree = createTextView(biere.getDegree() > 0 ? biere.getDegree() + "" : "", Gravity.CENTER);
		rating = createTextView(biere.isDrunk() ? biere.getRating() + "" : "", Gravity.CENTER);
		ratingDate = createTextView(biere.isDrunk() ? biere.getRatingDate() : "", Gravity.CENTER);

		addView(name);
		addView(degree);
		addView(rating);
		addView(ratingDate);

		manageBackgroundColor();
		setOnClickListener(new BiereListener(this));
		setOnLongClickListener(new BiereLongListener());
	}

	private TextView createTextView(String value, int gravity) {
		TextView text = new TextView(context);
		text.setText(value);
		text.setGravity(gravity);
		text.setPadding(4, 4, 0, 0);
		return text;
	}

	private void manageBackgroundColor() {
		if (biere.isDrunk()) {
			setBackgroundColor(getResources().getColor(R.color.drunk));
		} else {
			setBackgroundColor(getResources().getColor(R.color.white));
		}
	}

	class BiereLongListener implements OnLongClickListener {
		public boolean onLongClick(View v) {
			if (biere.isCustom()) {
				DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case DialogInterface.BUTTON_POSITIVE:
							Factory.get().getBeerReferential().deleteBeer(biere);
							Factory.get().getFragmentCave().initList();
							dialog.dismiss();
							break;
						case DialogInterface.BUTTON_NEGATIVE:
							dialog.dismiss();
							break;
						}
					}
				};
				AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
				builder.setMessage(biere.getName() + getContext().getString(R.string.delete_beer)).setPositiveButton(getContext().getString(R.string.button_validate), dialogClickListener).setNegativeButton(getContext().getString(R.string.button_cancel), dialogClickListener).show();
			}
			return false;
		}
	}

	class BiereListener implements OnClickListener {

		private BiereTableRow btr;

		public BiereListener(BiereTableRow btr) {
			super();
			this.btr = btr;
		}

		public void onClick(View v) {
			FragmentTransaction ft = Factory.get().getFragmentCave().getActivity().getFragmentManager().beginTransaction();
			BeerRatingPopup.newInstance(btr).show(ft, "Rating");
		}

	}

	public Beer getBiere() {
		return biere;
	}

	public void updateDrunk() {
		if (biere.isDrunk()) {
			rating.setText(biere.getRating() + "");
			ratingDate.setText(biere.getRatingDate());
		} else {
			rating.setText("");
			ratingDate.setText("");
		}
		Factory.get().getFragmentCave().showCount();
		Factory.get().getBeerReferential().saveRating(biere);
		manageBackgroundColor();
	}

}
