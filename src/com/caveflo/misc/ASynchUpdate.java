package com.caveflo.misc;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.caveflo.R;

public class ASynchUpdate extends AsyncTask<Void, Integer, Boolean> {

	private Context context;

	public ASynchUpdate(Context context) {
		super();
		this.context = context;
	}

	protected void onPreExecute() {
		super.onPreExecute();
		Toast.makeText(context, context.getString(R.string.update_inprogress), Toast.LENGTH_LONG).show();
	}

	protected void onProgressUpdate(Integer... values) {
		super.onProgressUpdate(values);
	}

	protected Boolean doInBackground(Void... arg0) {
		return Factory.get().getBeerReferential().update();
	}

	protected void onPostExecute(Boolean result) {
		if (result) {
			Factory.get().getFragmentCave().initList();
			Toast.makeText(context, context.getString(R.string.update_ok), Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(context, context.getString(R.string.update_ko), Toast.LENGTH_SHORT).show();
		}
	}

}