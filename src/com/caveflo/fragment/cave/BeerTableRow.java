package com.caveflo.fragment.cave;

import java.io.Serializable;

import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Typeface;
import android.text.TextUtils.TruncateAt;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.caveflo.R;
import com.caveflo.dao.Beer;
import com.caveflo.fragment.dialog.BeerRatingPopup;
import com.caveflo.fragment.dialog.CustomBeerPopup;
import com.caveflo.misc.Factory;

public class BeerTableRow extends TableRow implements Serializable {

	private static final long serialVersionUID = -4037399769129818273L;
	private Beer beer;
	private Context context;
	private TextView rating, ratingDate, name, degree, type;

	public BeerTableRow(Context context) {
		super(context);
		this.context = context;
	}

	public void setBeer(Beer beer) {
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

		setPadding(2, 2, 2, 2);
		setOnTouchListener(new BiereListener(this));
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
		text.setPadding(2, 0, 2, 0);
		text.setSingleLine(true);
		text.setEllipsize(TruncateAt.END);
		return text;
	}

	class BiereListener implements OnTouchListener {
		private BeerTableRow btr;

		float oldX = -1;
		float newX;
		float eventDuration;

		public BiereListener(BeerTableRow btr) {
			super();
			this.btr = btr;
		}

		public boolean onTouch(View v, MotionEvent event) {
			if (event.getAction() == MotionEvent.ACTION_MOVE) {
				newX = event.getX();
				if (oldX == -1) {
					oldX = newX;
				} else if (newX - oldX > (Factory.get().getDisplayMetrics().widthPixels * 0.25f)) {
					Factory.get().getFragmentAlcoolemie().addBeer(beer);
					Toast.makeText(getContext(), getContext().getString(R.string.drink_beer_added, beer.getName()), Toast.LENGTH_LONG).show();
					oldX = newX;
				}
			} else if (event.getAction() == MotionEvent.ACTION_UP) {
				eventDuration = event.getEventTime() - event.getDownTime();
				Log.d("BeerTableRow", "onTouch, duration: " + eventDuration);
				if (eventDuration > 700) { // Long press
					if (beer.isCustom()) {
						FragmentTransaction ft = Factory.get().getFragmentCave().getActivity().getFragmentManager().beginTransaction();
						CustomBeerPopup.newInstance(beer).show(ft, "Modify");
					}
				} else if (eventDuration < 100) {
					FragmentTransaction ft = Factory.get().getFragmentCave().getActivity().getFragmentManager().beginTransaction();
					BeerRatingPopup.newInstance(btr).show(ft, "Rating");
				}
			}
			return true;
		}
	}

	public Beer getBeer() {
		return beer;
	}

}
