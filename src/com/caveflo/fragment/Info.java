package com.caveflo.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.caveflo.R;

public class Info extends Fragment {

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View mainView = inflater.inflate(R.layout.info, container, false);
		return mainView;
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Button buttonNavigation = (Button) getActivity().findViewById(R.id.buttonNavigation);
		buttonNavigation.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Uri request = Uri.parse("google.navigation:q=" + getString(R.string.navigation));
				Intent i = new Intent(Intent.ACTION_VIEW, request);
				startActivity(i);
			}
		});
	}
}
