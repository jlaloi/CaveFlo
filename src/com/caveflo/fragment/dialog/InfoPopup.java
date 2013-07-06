package com.caveflo.fragment.dialog;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.caveflo.R;

public class InfoPopup extends DialogFragment {

	public static final String textParameter = "textParameter";

	private TextView infoText;

	public static InfoPopup newInstance(String text) {
		InfoPopup dialog = new InfoPopup();
		Bundle arg = new Bundle();
		arg.putString(textParameter, text);
		dialog.setArguments(arg);
		return dialog;
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.infopopup, container, false);
		getDialog().setTitle(getString(R.string.app_name));
		infoText = ((TextView) v.findViewById(R.id.infotext));
		infoText.setText(getArguments().getString(textParameter));
		return v;
	}

	public void setText(String text) {
		infoText.setText(text);
	}

}
