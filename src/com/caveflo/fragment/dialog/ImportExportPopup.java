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

	private EditText directory, userbeer, userrating;
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
		userbeer = (EditText) v.findViewById(R.id.userbeerfile);
		userrating = (EditText) v.findViewById(R.id.userratingfile);
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
					importConfig();
				} else if (actionRadio.getCheckedRadioButtonId() == R.id.radioexport) {
					exportingConfig();
				}
				dismiss();
			}
		});
		return v;
	}

	private File getBeerFile() {
		String path = (directory.getText() + "/" + userbeer.getText()).replace("//", "/");
		return new File(Environment.getExternalStorageDirectory(), path);
	}

	private File getRatingFile() {
		String path = (directory.getText() + "/" + userrating.getText()).replace("//", "/");
		return new File(Environment.getExternalStorageDirectory(), path);
	}

	public void importConfig() {
		File beerFile = getBeerFile();
		if (beerFile.exists()) {
			Factory.get().getBeerReferential().update(beerFile);
			File ratingFile = getRatingFile();
			if (ratingFile.exists()) {
				Factory.get().getBeerReferential().updateRating(ratingFile);
				Toast.makeText(getActivity(), getString(R.string.filesimportok), Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(getActivity(), getString(R.string.filenotfound, ratingFile.getAbsolutePath()), Toast.LENGTH_LONG).show();
			}
			Factory.get().getFragmentCave().initList();
		} else {
			Toast.makeText(getActivity(), getString(R.string.filenotfound, beerFile.getAbsolutePath()), Toast.LENGTH_LONG).show();
		}
	}

	public void exportingConfig() {
		new File(Environment.getExternalStorageDirectory(), directory.getText().toString()).mkdirs();
		int nbBeer = Factory.get().getBeerReferential().saveCustomBeerToFile(getBeerFile());
		int nbRating = Factory.get().getBeerReferential().saveRatingToFile(getRatingFile());
		Toast.makeText(getActivity(), getString(R.string.exportresult, nbBeer, nbRating), Toast.LENGTH_LONG).show();
	}

}
