package com.caveflo;

import java.util.HashMap;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;

import com.caveflo.fragment.dialog.BeerCreatePopup;
import com.caveflo.misc.Factory;

public class MainActivity extends Activity implements TabListener {

	private HashMap<String, Fragment> content = new HashMap<String, Fragment>();

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
		setContentView(R.layout.activity_main);
		getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		content.put(getString(R.string.tab_cave), Factory.get().getFragmentCave());
		content.put(getString(R.string.tab_news), Factory.get().getFragmentNews());
		content.put(getString(R.string.tab_info), Factory.get().getFragmentInfo());

		getActionBar().addTab(getActionBar().newTab().setText(getString(R.string.tab_cave)).setTabListener(this));
		getActionBar().addTab(getActionBar().newTab().setText(getString(R.string.tab_news)).setTabListener(this));
		getActionBar().addTab(getActionBar().newTab().setText(getString(R.string.tab_info)).setTabListener(this));
	}

	public void onTabSelected(Tab tab, android.app.FragmentTransaction ft) {
		ft.replace(R.id.fragmentContainer, content.get(tab.getText()));
	}

	public void onTabUnselected(Tab tab, android.app.FragmentTransaction ft) {
	}

	public void onTabReselected(Tab tab, android.app.FragmentTransaction ft) {
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.layout.menu, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_add_beer:
			FragmentTransaction ft = Factory.get().getFragmentCave().getActivity().getFragmentManager().beginTransaction();
			BeerCreatePopup.newInstance().show(ft, "create");
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
