package com.caveflo.cave;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.os.Environment;

import com.caveflo.R;
import com.caveflo.misc.Factory;
import com.caveflo.misc.Tools;

public class CaveDB {
	public static final String caveDir = "Caveflo/";
	public static final String caveFileName = caveDir + "caveflo.ext";
	public static final String caveUserFileName = caveDir + "ratingUser.ext";
	public static final String beerUserFileName = caveDir + "beerUser.ext";
	public static final String dlCaveFile = "https://github.com/jlaloi/CaveFlo/raw/master/caveflodb.csv";
	public static final String fileSep = ";";
	private File caveFile, caveUserFile;
	private List<Biere> db;
	private HashMap<String, UserSavedData> userCave;

	public CaveDB() {
		new File(Environment.getExternalStorageDirectory(), caveDir).mkdirs();
		caveFile = new File(Environment.getExternalStorageDirectory(), caveFileName);
		caveUserFile = new File(Environment.getExternalStorageDirectory(), caveUserFileName);
		userCave = new HashMap<String, UserSavedData>();
	}

	public void shouldUpdate(Activity activity) {
		if (!caveFile.exists()) {
			update(activity);
		}
	}

	public void update(Activity activity) {
		if (Tools.downloadFile(dlCaveFile, caveFile)) {
			Factory.get().createNotification(activity, activity.getString(R.string.app_name), activity.getString(R.string.update_ok));
		} else {
			Factory.get().createNotification(activity, activity.getString(R.string.app_name), activity.getString(R.string.update_ko));
		}
	}

	public void saveDrunk(Biere biere) {
		userCave.put(biere.getId(), new UserSavedData(biere));
		saveUserCave();
	}

	public List<Biere> readDb(Activity activity) {
		if (db == null || db.size() == 0) {
			loadUserCave();
			db = new ArrayList<Biere>();
			shouldUpdate(activity);
			for (String line : Tools.read(caveFile)) {
				String[] split = line.split(fileSep);
				if (split.length > 3) {
					Biere biere = new Biere(split[0], split[1], Float.valueOf(split[2]), split[3]);
					if (userCave.containsKey(biere.getId())) {
						biere.setDrunk(userCave.get(biere.getId()).getDrunk());
						biere.setRating(userCave.get(biere.getId()).getRating());
					}
					db.add(biere);
				}
			}
			Collections.sort(db, Factory.get().getBiereComparator());
		}
		return db;
	}

	public void loadUserCave() {
		userCave.clear();
		if (!caveUserFile.exists()) {
			try {
				caveUserFile.createNewFile();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			for (String line : Tools.read(caveUserFile)) {
				String[] split = line.split(fileSep);
				if (split.length > 2) {
					userCave.put(split[0], new UserSavedData(split[0], split[1], Integer.valueOf(split[2])));
				}
			}
		}
	}

	public void saveUserCave() {
		List<String> toSave = new ArrayList<String>();
		for (String key : userCave.keySet()) {
			toSave.add(key + fileSep + userCave.get(key).getDrunk() + fileSep + userCave.get(key).getRating());
		}
		Tools.write(caveUserFile, toSave);
	} 
	
	public List<Biere> getUserBeer(){
		return null;
	}
	
	public String addUserBeer(String name, Float degree){
		return "id";		
	}

}
