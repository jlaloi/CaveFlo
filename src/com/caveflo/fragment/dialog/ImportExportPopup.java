package com.caveflo.fragment.dialog;

import java.io.File;

import android.app.DialogFragment;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.caveflo.R;
import com.caveflo.misc.Factory;

public class ImportExportPopup extends DialogFragment {

	private EditText directory;
	private RadioGroup actionRadio;

	public static ImportExportPopup newInstance() {
		ImportExportPopup dialog = new ImportExportPopup();
		dialog.setArguments(new Bundle());
		return dialog;
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.importexport, container, false);
		getDialog().setTitle(getString(R.string.importexporttile));

		directory = (EditText) v.findViewById(R.id.expimpdirectory);
		actionRadio = (RadioGroup) v.findViewById(R.id.impexpradiogroup);

		Button buttonCancel = (Button) v.findViewById(R.id.buttonexpimpcancel);
		buttonCancel.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				dismiss();
			}
		});

		Button buttonValidate = (Button) v.findViewById(R.id.buttonexpimpdo);
		buttonValidate.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				if (actionRadio.getCheckedRadioButtonId() == R.id.radioimport) {
					importConfig(directory.getText().toString());
				} else if (actionRadio.getCheckedRadioButtonId() == R.id.radioexport) {
					exportingConfig(directory.getText().toString());
				}
				dismiss();
			}
		});

		return v;
	}

	public void importConfig(String path) {
		// Import beer
		String beerFilePath = path + "/" + getString(R.string.userbeerfile).replace("//", "/");
		File beerFile = new File(Environment.getExternalStorageDirectory(), beerFilePath);
		if (beerFile.exists()) {
			Factory.get().getBeerReferential().update(beerFile);
			// Import rating
			String ratingFilePath = path + "/" + getString(R.string.userratingfile).replace("//", "/");
			File ratingFile = new File(Environment.getExternalStorageDirectory(), ratingFilePath);
			if (ratingFile.exists()) {
				Factory.get().getBeerReferential().updateRating(ratingFile);
				Toast.makeText(getActivity(), getString(R.string.filesimportok), Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(getActivity(), getString(R.string.filenotfound, ratingFilePath), Toast.LENGTH_LONG).show();
			}
		} else {
			Toast.makeText(getActivity(), getString(R.string.filenotfound, beerFilePath), Toast.LENGTH_LONG).show();
		}
		Factory.get().getFragmentCave().initList();
	}

	public void exportingConfig(String path) {
		Toast.makeText(getActivity(), getString(R.string.notimplemented), Toast.LENGTH_LONG).show();
	}

}
