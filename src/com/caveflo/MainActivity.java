package com.caveflo;

import com.caveflo.R;
import com.caveflo.fragment.Cave;
import com.caveflo.fragment.Info;
import com.caveflo.fragment.News;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

public class MainActivity extends Activity implements TabListener {

	private Cave frag1 = new Cave("Cave","cave");
	private News frag2 = new News("News", "news");
	private Info frag3 = new Info("Info", "info");
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
		setContentView(R.layout.activity_main);

		getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		getActionBar().addTab(getActionBar().newTab().setText(frag1.getName()).setTabListener(this));
		getActionBar().addTab(getActionBar().newTab().setText(frag2.getName()).setTabListener(this));
		getActionBar().addTab(getActionBar().newTab().setText(frag3.getName()).setTabListener(this));

	}

	public void onTabSelected(Tab tab, android.app.FragmentTransaction ft) {
		if (tab.getText().equals(frag1.getName())) {
			ft.replace(R.id.fragmentContainer, frag1);
		} else if (tab.getText().equals(frag2.getName())) {
			ft.replace(R.id.fragmentContainer, frag2);
		} else if (tab.getText().equals(frag3.getName())) {
			ft.replace(R.id.fragmentContainer, frag3);
		}
	}

	public void onTabUnselected(Tab tab, android.app.FragmentTransaction ft) {
		if (tab.getText().equals(frag1.getName())) {
			ft.remove(frag1);
		} else if (tab.getText().equals(frag2.getName())) {
			ft.remove(frag2);
		} else if (tab.getText().equals(frag3.getName())) {
			ft.remove(frag3);
		}
	}

	public void onTabReselected(Tab tab, android.app.FragmentTransaction ft) {
	}

}
