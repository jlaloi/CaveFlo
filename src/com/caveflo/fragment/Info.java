package com.caveflo.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.caveflo.R;
import com.caveflo.misc.Factory;

public class Info extends Fragment {

	public static final String layout = "info";

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(getResources().getIdentifier(layout, "layout", container.getContext().getPackageName()), container, false);
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Button buttonStart = (Button) getActivity().findViewById(R.id.buttonRefresh);
		buttonStart.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Factory.get().getCaveDB().update(getActivity());
			}
		});
	}
}
