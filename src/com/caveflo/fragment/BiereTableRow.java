package com.caveflo.fragment;

import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.widget.TableRow;
import android.widget.TextView;

import com.caveflo.R;
import com.caveflo.cave.Biere;
import com.caveflo.fragment.dialog.BeerRatingPopup;
import com.caveflo.misc.Factory;

public class BiereTableRow extends TableRow {

	private Biere biere;
	private Context context;
	private TextView rating, drunk;

	public BiereTableRow(Context context, Biere biere) {
		super(context);
		this.biere = biere;
		this.context = context;

		TextView name = createTextView(biere.getName(), Gravity.LEFT);
		if (biere.isCustom()) {
			System.out.println("Custom");
			name.setTypeface(null, Typeface.BOLD_ITALIC);
		}
		TextView degree = createTextView(biere.getDegree() > 0 ? biere.getDegree() + "" : "", Gravity.CENTER);
		rating = createTextView(biere.isDrunk() ? biere.getRating() + "" : "", Gravity.CENTER);
		drunk = createTextView(biere.isDrunk() ? biere.getDrunk() : "", Gravity.CENTER);

		addView(name, 0);
		addView(degree, 1);
		addView(rating, 2);
		addView(drunk, 3);

		manageBackgroundColor();
		setOnClickListener(new BiereListener(this));
		setOnLongClickListener(new BiereLongListener());
	}

	private TextView createTextView(String value, int gravity) {
		TextView text = new TextView(context, null, R.style.frag2TableRow);
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
							Factory.get().getCaveDB().deleteCustomBeer(biere.getId());
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

	public Biere getBiere() {
		return biere;
	}

	public void updateDrunk() {
		if (biere.isDrunk()) {
			rating.setText(biere.getRating() + "");
			drunk.setText(biere.getDrunk());
		} else {
			rating.setText("");
			drunk.setText("");
		}
		Factory.get().getFragmentCave().showCount();
		Factory.get().getCaveDB().saveDrunk(biere);
		manageBackgroundColor();
	}

}
