package com.caveflo;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.caveflo.fragment.dialog.CustomBeerPopup;
import com.caveflo.fragment.dialog.ImportExportPopup;
import com.caveflo.misc.ASynchUpdate;
import com.caveflo.misc.Factory;

public class MainActivity extends Activity {

	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	private String mTitle = "";
	private String[] tabs;
	public static int currentFragment = 0;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		tabs = getResources().getStringArray(R.array.nav_menu);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.drawer_list);

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(getString(R.string.app_name));
				invalidateOptionsMenu();
			}
		};

		mDrawerLayout.setDrawerListener(mDrawerToggle);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getBaseContext(), R.layout.drawer_list_item, getResources().getStringArray(R.array.nav_menu));
		mDrawerList.setAdapter(adapter);
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		mDrawerList.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				setContent(position);
			}
		});
	}

	public void onResume() {
		super.onResume();
		Log.i("MainActivity", "Resuming " + currentFragment);
		getWindowManager().getDefaultDisplay().getMetrics(Factory.get().getDisplayMetrics());
		Factory.get().initiateBeerDataSource(getBaseContext());
		setContent(currentFragment);
	}

	private void setContent(int fragment) {
		Log.i("MainActivity", "Setting fragment " + fragment);
		mDrawerList.setItemChecked(fragment, true);
		mTitle = tabs[fragment];
		mDrawerLayout.closeDrawer(mDrawerList);
		getActionBar().setTitle(mTitle);
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction ft = fragmentManager.beginTransaction();
		ft.replace(R.id.content_frame, getFragment(fragment));
		ft.commit();
		currentFragment = fragment;
		Log.d("MainActivity", "Current Fragment set to " + fragment);
	}

	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		FragmentTransaction ft;
		switch (item.getItemId()) {
		case R.id.menu_add_beer:
			ft = this.getFragmentManager().beginTransaction();
			CustomBeerPopup.newInstance(null).show(ft, "create");
			return true;
		case R.id.menu_refresh:
			new ASynchUpdate(getBaseContext()).execute();
			return true;
		case R.id.menu_impexp:
			ft = this.getFragmentManager().beginTransaction();
			ImportExportPopup.newInstance().show(ft, "importexport");
			return true;
		case R.id.menu_quit:
			finish();
			return true;
		}
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		return super.onOptionsItemSelected(item);

	}

	public void finish() {
		Factory.get().getBeerDataSource().close();
		super.finish();
	}

	public boolean onMenuOpened(int featureId, Menu menu) {
		menu.findItem(R.id.menu_add_beer).setVisible(currentFragment == 0);
		menu.findItem(R.id.menu_impexp).setVisible(currentFragment == 0);
		menu.findItem(R.id.menu_refresh).setVisible(currentFragment == 0);
		return super.onMenuOpened(featureId, menu);
	}

	public boolean onPrepareOptionsMenu(Menu menu) {
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public Fragment getFragment(int id) {
		switch (id) {
		case 0:
			return Factory.get().getFragmentCave();
		case 1:
			return Factory.get().getFragmentNews();
		case 2:
			return Factory.get().getFragmentInfo();
		case 3:
			return Factory.get().getFragmentAlcoolemie();
		}
		return Factory.get().getFragmentCave();
	}

}
