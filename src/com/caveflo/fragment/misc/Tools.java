package com.caveflo.fragment.misc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.os.StrictMode;

public class Tools {
	
	static{
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
	}
	
	public static List<String> read(File file) {
		List<String> result = new ArrayList<String>();
		try {
			FileInputStream objFile = new FileInputStream(file);
			InputStreamReader objReader = new InputStreamReader(objFile);
			BufferedReader objBufferReader = new BufferedReader(objReader);
			String strLine;
			while ((strLine = objBufferReader.readLine()) != null) {
				result.add(strLine);
			}
			objFile.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public static void write(File file, List<String> lines) {
		FileWriter writer = null;
		try {
			writer = new FileWriter(file, false);
			for (String line : lines) {
				writer.write(line + "\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void download(String adress, File file) {
		FileOutputStream fileOutput = null;
		InputStream inputStream = null;
		try {
			URL url = new URL(adress);
			HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.setDoOutput(true);
			urlConnection.connect();
			fileOutput = new FileOutputStream(file);
			inputStream = urlConnection.getInputStream();
			byte[] buffer = new byte[1024];
			int bufferLength = 0;
			while ((bufferLength = inputStream.read(buffer)) > 0) {
				fileOutput.write(buffer, 0, bufferLength);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (fileOutput != null) {
					fileOutput.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
