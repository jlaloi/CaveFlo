package com.caveflo;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.os.Environment;
import android.widget.Toast;

import com.caveflo.misc.Tools;

public class CaveDB {
	public static final String caveDir = "Caveflo/";
	public static final String caveFileName = caveDir + "caveflo.ext";
	public static final String caveUserFileName = caveDir + "cavefloUser.ext";
	public static final String dlCaveFile = "https://github.com/jlaloi/CaveFlo/raw/master/caveflodb.csv";
	public static final String fileSep = ";";
	private File caveFile, caveUserFile;
	private Activity activity;
	private List<Biere> db;
	private HashMap<String, String> userCave;

	public CaveDB(Activity activity) {
		this.activity = activity;
		new File(Environment.getExternalStorageDirectory(),caveDir).mkdirs();
		caveFile = new File(Environment.getExternalStorageDirectory(),caveFileName);
		caveUserFile = new File(Environment.getExternalStorageDirectory(),caveUserFileName);
		userCave = new HashMap<String, String>();
	}
	
	public void shouldUpdate() {
		if (!caveFile.exists()) {
			update();
		}
	}

	public void update() {
		Toast.makeText(activity, "Rafraîchissement de la liste de la cave",Toast.LENGTH_SHORT).show();
		Tools.downloadFile(dlCaveFile, caveFile);
		Toast.makeText(activity, "Cave à jour!", Toast.LENGTH_SHORT).show();
	}
	
	public void saveDrunk(Biere biere){
		userCave.put(biere.getId(), biere.getDrunk());
		saveUserCave();
	}

	public List<Biere> readDb() {
		if (db == null || db.size() == 0) {
			loadUserCave();
			db = new ArrayList<Biere>();
			shouldUpdate();
			for (String line : Tools.read(caveFile)) {
				String[] split = line.split(fileSep);
				if (split.length > 3) {
					Biere biere = new Biere(split[0], split[1],Float.valueOf(split[2]), split[3]);
					if(userCave.containsKey(biere.getId())){
						biere.setDrunk(userCave.get(biere.getId()));
					}
					db.add(biere);
				}
			}
			Collections.sort(db, new Biere());
		}
		return db;
	}
	
	public void loadUserCave(){
		userCave.clear();
		if(!caveUserFile.exists()){
			try {
				caveUserFile.createNewFile();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			for(String line: Tools.read(caveUserFile)){
				String[] split = line.split(fileSep);
				if (split.length > 1) {
					userCave.put(split[0], split[1]);
				}
			}
		}
	}
	
	public void saveUserCave(){
		List<String> toSave = new ArrayList<String>();
		for(String key: userCave.keySet()){
			toSave.add(key + fileSep + userCave.get(key));
		}
		Tools.write(caveUserFile, toSave);
	}

	

}
