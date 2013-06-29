package com.caveflo.misc;

import java.util.Comparator;

import android.app.Activity;
import android.app.Fragment;
import android.app.Notification;
import android.app.NotificationManager;

import com.caveflo.R;
import com.caveflo.cave.Biere;
import com.caveflo.cave.CaveDB;
import com.caveflo.fragment.Cave;
import com.caveflo.fragment.Info;
import com.caveflo.fragment.News;

public class Factory {

	private static final Factory instance = new Factory();

	public static Factory get() {
		return instance;
	}

	private Cave fragmentCave;
	private Fragment fragmentInfo;
	private Fragment fragmentNews;

	private CaveDB caveDB;

	private Comparator<Biere> biereComparator;

	public Factory() {
		fragmentCave = new Cave();
		fragmentInfo = new Info();
		fragmentNews = new News();
		caveDB = new CaveDB();
		biereComparator = new Comparator<Biere>() {
			public int compare(Biere lhs, Biere rhs) {
				return lhs.getName().toLowerCase().compareTo(rhs.getName().toLowerCase());
			}
		};
	}

	public void createNotification(Activity activity, String title, String text) {
		Notification notification = new Notification.Builder(activity).setContentTitle(title).setContentText(text).setSmallIcon(R.drawable.ic_launcher).build();
		NotificationManager notificationManager = (NotificationManager) activity.getSystemService(Activity.NOTIFICATION_SERVICE);
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		notification.defaults = Notification.DEFAULT_ALL;
		notificationManager.notify(0, notification);
	}

	public CaveDB getCaveDB() {
		return caveDB;
	}

	public static Factory getInstance() {
		return instance;
	}

	public Cave getFragmentCave() {
		return fragmentCave;
	}

	public Fragment getFragmentInfo() {
		return fragmentInfo;
	}

	public Fragment getFragmentNews() {
		return fragmentNews;
	}

	public Comparator<Biere> getBiereComparator() {
		return biereComparator;
	}
}
