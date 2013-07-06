package com.caveflo;

import java.util.HashMap;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.caveflo.fragment.dialog.BeerCreatePopup;
import com.caveflo.misc.Factory;

public class MainActivity extends Activity {

	private HashMap<String, Fragment> content = new HashMap<String, Fragment>();
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	private String mTitle = "";

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Factory.get().initiateBeerDataSource(getApplicationContext());
		content.put(getString(R.string.tab_cave), Factory.get().getFragmentCave());
		content.put(getString(R.string.tab_news), Factory.get().getFragmentNews());
		content.put(getString(R.string.tab_info), Factory.get().getFragmentInfo());

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
		setContent(0);
	}
	
	private void setContent(int fragment){
		String[] tabs = getResources().getStringArray(R.array.nav_menu);
		mTitle = tabs[fragment];
		Bundle data = new Bundle();
		data.putInt("position", fragment);
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction ft = fragmentManager.beginTransaction();
		ft.replace(R.id.content_frame, content.get(mTitle));
		ft.commit();
		mDrawerLayout.closeDrawer(mDrawerList);
		getActionBar().setTitle(mTitle);
	}

	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		return super.onOptionsItemSelected(item);
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
	
	public void addBeer(View v) {
		FragmentTransaction ft = this.getFragmentManager().beginTransaction();
		BeerCreatePopup.newInstance().show(ft, "create");
	}
	
	public void refreshCave(View v){
		if(Factory.get().getBeerReferential().update()){
			Factory.get().getFragmentCave().filterList();
			Toast.makeText(getBaseContext(), getString(R.string.update_ok), Toast.LENGTH_SHORT).show();
		}else{
			Toast.makeText(getBaseContext(), getString(R.string.update_ko), Toast.LENGTH_SHORT).show();
		}
	}

}
