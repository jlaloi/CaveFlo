package com.caveflo.cave;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import android.os.Environment;
import android.widget.Toast;

import com.caveflo.R;
import com.caveflo.misc.Factory;
import com.caveflo.misc.Tools;

public class CaveDB {

	public static final String caveDir = "Caveflo/";
	public static final String caveFileName = caveDir + "caveflo.ext";
	public static final String caveUserFileName = caveDir + "ratingUser.ext";
	public static final String customBeerFileName = caveDir + "customBeer.ext";
	public static final String dlCaveFile = "https://github.com/jlaloi/CaveFlo/raw/master/caveflodb.csv";
	public static final String fileSep = ";";
	private File caveFile, caveUserFile, customBeerFile;
	private List<Biere> db;
	private HashMap<String, UserSavedData> userCave;
	private HashMap<String, Biere> customBeer;

	public CaveDB() {
		new File(Environment.getExternalStorageDirectory(), caveDir).mkdirs();
		caveFile = new File(Environment.getExternalStorageDirectory(), caveFileName);
		caveUserFile = new File(Environment.getExternalStorageDirectory(), caveUserFileName);
		customBeerFile = new File(Environment.getExternalStorageDirectory(), customBeerFileName);
		userCave = new HashMap<String, UserSavedData>();
		customBeer = new HashMap<String, Biere>();
	}

	public void shouldUpdate() {
		if (!caveFile.exists()) {
			update();
		}
	}

	public boolean update() {
		return Tools.downloadFile(dlCaveFile, caveFile);
	}

	public void saveDrunk(Biere biere) {
		userCave.put(biere.getId(), new UserSavedData(biere));
		saveUserCave();
	}

	public List<Biere> readDb() {
		if (db == null || db.size() == 0) {
			loadUserCave();
			loadCustomBeer();
			db = new ArrayList<Biere>();
			shouldUpdate();
			// Add cave beers
			for (String line : Tools.read(caveFile)) {
				String[] split = line.split(fileSep);
				if (split.length > 3) {
					db.add(new Biere(split[0], split[1], Float.valueOf(split[2]), split[3]));
				}
			}
			// Add custom beers
			for (String key : customBeer.keySet()) {
				db.add(customBeer.get(key));
			}
			// Add user rating
			for (Biere biere : db) {
				if (userCave.containsKey(biere.getId())) {
					biere.setDrunk(userCave.get(biere.getId()).getDrunk());
					biere.setRating(userCave.get(biere.getId()).getRating());
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

	public HashMap<String, Biere> loadCustomBeer() {
		customBeer.clear();
		if (!customBeerFile.exists()) {
			try {
				customBeerFile.createNewFile();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			for (String line : Tools.read(customBeerFile)) {
				String[] split = line.split(fileSep);
				if (split.length > 2) {
					Biere biere = new Biere(split[0], split[1], Float.valueOf(split[2]));
					biere.setCustom(true);
					customBeer.put(split[0], biere);
				}
			}
		}
		return customBeer;
	}

	public String addCustomBeer(String name, Float degree) {
		String id = "#" + Calendar.getInstance().getTimeInMillis();
		Biere biere = new Biere(id, name, degree);
		biere.setCustom(true);
		customBeer.put(id, biere);
		saveCustomeBeer();
		db.add(biere);
		Collections.sort(db, Factory.get().getBiereComparator());
		Toast.makeText(Factory.get().getFragmentCave().getActivity(), name + " " + Factory.get().getFragmentCave().getString(R.string.create_confirm), Toast.LENGTH_LONG).show();
		return id;
	}

	public void saveCustomeBeer() {
		List<String> toSave = new ArrayList<String>();
		for (String key : customBeer.keySet()) {
			toSave.add(key + fileSep + customBeer.get(key).getName() + fileSep + customBeer.get(key).getDegree());
		}
		Tools.write(customBeerFile, toSave);
	}

	public void deleteCustomBeer(String id) {
		String name = customBeer.get(id).getName();
		db.remove(customBeer.get(id));
		customBeer.remove(id);
		saveCustomeBeer();
		Toast.makeText(Factory.get().getFragmentCave().getActivity(), name + " " + Factory.get().getFragmentCave().getString(R.string.delete_confirm), Toast.LENGTH_LONG).show();
	}

}
