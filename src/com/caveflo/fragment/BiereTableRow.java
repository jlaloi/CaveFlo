package com.caveflo.fragment;

import java.io.Serializable;

import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.TableRow;
import android.widget.TextView;

import com.caveflo.R;
import com.caveflo.dao.Beer;
import com.caveflo.fragment.dialog.BeerRatingPopup;
import com.caveflo.fragment.dialog.CustomBeerPopup;
import com.caveflo.misc.Factory;

public class BiereTableRow extends TableRow implements Serializable {

	private static final long serialVersionUID = -4037399769129818273L;
	private Beer beer;
	private Context context;
	private TextView rating, ratingDate, name, degree, type;

	public BiereTableRow(Context context) {
		super(context);
	}

	public BiereTableRow(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public BiereTableRow(Context context, Beer beer) {
		super(context);
		this.context = context;

		name = createTextView(Gravity.LEFT);
		if (beer.isCustom()) {
			name.setTypeface(null, Typeface.BOLD_ITALIC);
		}
		degree = createTextView(Gravity.CENTER);
		type = createTextView(Gravity.LEFT);
		rating = createTextView(Gravity.CENTER);
		ratingDate = createTextView(Gravity.CENTER);

		updateBeer(beer);
		
		addView(name);
		addView(type);
		addView(degree);
		addView(rating);
		addView(ratingDate);

		setOnClickListener(new BiereListener(this));
		setOnLongClickListener(new BiereLongListener());
	}

	public void onBeerUpdate() {
		updateBeer(beer);
	}

	public void updateBeer(Beer beer) {
		this.beer = beer;
		name.setText(beer.getName());
		degree.setText(beer.getDegree() > 0 ? beer.getDegree() + "" : "");
		type.setText(beer.getType());
		if (beer.isDrunk()) {
			rating.setText(beer.isDrunk() ? beer.getRating() + "" : "");
			ratingDate.setText(beer.isDrunk() ? beer.getRatingDate() : "");
			setBackgroundColor(getResources().getColor(R.color.drunk));
		} else {
			rating.setText("");
			ratingDate.setText("");
			setBackgroundColor(getResources().getColor(R.color.notdrunk));
		}
	}

	private TextView createTextView(int gravity) {
		TextView text = new TextView(context);
		text.setGravity(gravity);
		text.setPadding(4, 4, 0, 0);
		return text;
	}

	class BiereLongListener implements OnLongClickListener {
		public boolean onLongClick(View v) {
			if (beer.isCustom()) {
				FragmentTransaction ft = Factory.get().getFragmentCave().getActivity().getFragmentManager().beginTransaction();
				CustomBeerPopup.newInstance(beer).show(ft, "Modify");
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

	public Beer getBeer() {
		return beer;
	}

}
