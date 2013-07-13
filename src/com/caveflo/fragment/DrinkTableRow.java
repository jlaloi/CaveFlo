package com.caveflo.fragment;

import com.caveflo.R;
import com.caveflo.misc.Factory;

import android.content.Context;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableRow;

public class DrinkTableRow extends TableRow {

	private EditText quantity, degree;
	private Context context;
	private TableRow me;

	public DrinkTableRow(Context context) {
		super(context);
		this.me = this;
		this.context = context;
		quantity = createEditText(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
		degree = createEditText(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
		ImageButton delete = new ImageButton(context);
		delete.setImageResource(R.drawable.ic_delete);
		delete.setBackground(null);
		delete.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Factory.get().getFragmentAlcoolemie().removeTableRow(me);
			}
		});
		addView(createEditText(InputType.TYPE_CLASS_TEXT));
		addView(quantity);
		addView(degree);
		addView(delete);
	}

	private EditText createEditText(int inputType) {
		EditText text = new EditText(context);
		text.setPadding(4, 4, 0, 0);
		text.setInputType(inputType);
		text.setGravity(Gravity.CENTER);
		return text;
	}

	public float getDegree() {
		float result = 0f;
		try {
			result = Float.valueOf(degree.getText().toString()) / 100f;
		} catch (Exception e) {
		}
		return result;
	}

	public float getQuantity() {
		float result = 0f;
		try {
			result = Float.valueOf(quantity.getText().toString()) * 10f;
		} catch (Exception e) {
		}
		return result;
	}

}
