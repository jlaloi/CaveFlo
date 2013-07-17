package com.caveflo.fragment.bloodalchohol;

import android.content.Context;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TableRow;

import com.caveflo.R;
import com.caveflo.dao.Drink;
import com.caveflo.misc.Factory;

public class DrinkTableRow extends TableRow {

	public static final int spinnerStyleId = android.R.layout.simple_spinner_item;

	private EditText quantity, degree, note;
	private Spinner hour;
	private Context context;
	private Drink drink;

	public DrinkTableRow(Context context) {
		super(context);
		this.context = context;
	}

	public void setDrink(Drink drink) {
		setPadding(0, 0, 0, 0);
		this.drink = drink;
		note = createEditText(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
		note.setEms(10);
		quantity = createEditText(InputType.TYPE_CLASS_NUMBER);
		degree = createEditText(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
		hour = new Spinner(context);
		hour.setPadding(4, 4, 0, 0);
		ArrayAdapter<String> hoursAdapter = new ArrayAdapter<String>(context, spinnerStyleId, getResources().getStringArray(R.array.alco_hour));
		hour.setAdapter(hoursAdapter);
		ImageButton delete = new ImageButton(context);
		delete.setImageResource(R.drawable.ic_delete);
		delete.setBackgroundResource(android.R.color.transparent);
		delete.setPadding(0, 0, 0, 0);
		delete.setOnClickListener(new DeleteListner(this));
		addView(note);
		addView(degree);
		addView(quantity);
		addView(hour);
		addView(delete);

		note.setText(drink.getNote());
		if (drink.getDegree() > 0) {
			degree.setText(drink.getDegree() + "");
		}
		if (drink.getQuantity() > 0) {
			quantity.setText(drink.getQuantity() + "");
		}
		hour.setSelection(drink.getHour());
	}

	private EditText createEditText(int inputType) {
		EditText text = new EditText(context);
		text.setPadding(4, 4, 0, 0);
		text.setInputType(inputType);
		text.setGravity(Gravity.CENTER);
		return text;
	}

	public Drink getDrink() {
		float degreeValue = 0f;
		try {
			degreeValue = Float.valueOf(degree.getText().toString());
		} catch (Exception e) {
		}
		int quantityValue = 0;
		try {
			quantityValue = Integer.valueOf(quantity.getText().toString());
		} catch (Exception e) {
		}
		drink.setNote(note.getText().toString());
		drink.setDegree(degreeValue);
		drink.setQuantity(quantityValue);
		drink.setHour(hour.getSelectedItemPosition());
		return drink;
	}

	class DeleteListner implements OnClickListener {

		private DrinkTableRow drinkTableRow;

		public DeleteListner(DrinkTableRow drinkTableRow) {
			super();
			this.drinkTableRow = drinkTableRow;
		}

		public void onClick(View arg0) {
			Factory.get().getFragmentAlcoolemie().removeTableRow(drinkTableRow);
		}

	}

}
