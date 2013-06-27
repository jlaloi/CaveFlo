package com.caveflo.fragment.misc;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.os.StrictMode;

public class Tools {
	
	public static final String userAgent = "Mozilla/5.0 (X11; U; Linux x86_64; en-US; rv:1.9.1.3)";
	
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

	public static void downloadFile(String url, File file) {
		BufferedInputStream in = null;
		FileOutputStream fout = null;
		try {
			HttpURLConnection urlc = (HttpURLConnection) new URL(url.replace(" ", "%20")).openConnection();
			urlc.setRequestProperty("User-Agent", userAgent);
			urlc.connect();
			in = new BufferedInputStream(urlc.getInputStream());
			fout = new FileOutputStream(file);
			byte data[] = new byte[1024];
			int count;
			while ((count = in.read(data, 0, 1024)) != -1) {
				fout.write(data, 0, count);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				fout.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
