package com.example.newplayer;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import android.content.res.AssetManager;
import android.util.Log;

import org.json.*;

public class PlayList {
	private static String url = "http://dev.beamto.us/albums.json?page=1";
	private static final String TAG_ID = "id";
	private static final String TAG_NAME = "name";
	private static final String TAG_LABEL_NAME = "label-name";
	private static final String TAG_ALBUM_IMAGE = "coverart_small";

	private ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();

	public ArrayList<HashMap<String, String>> songList() {
		try {
			JSONParser jParser = new JSONParser();

			/*
			 * JSONArray json = jParser.getJSONFromUrl(url); for (int i = 0; i <
			 * json.length(); i++) { HashMap<String, String> song = new
			 * HashMap<String, String>(); JSONObject songDetails =
			 * json.getJSONObject(i); String id = songDetails.getString(TAG_ID);
			 * String name = songDetails.getString(TAG_NAME); String labelName =
			 * songDetails.getString(TAG_LABEL_NAME); String albumImage =
			 * songDetails.getString(TAG_ALBUM_IMAGE); song.put(TAG_ID, id);
			 * song.put(TAG_NAME, name); song.put(TAG_LABEL_NAME, labelName);
			 * song.put(TAG_ALBUM_IMAGE, albumImage); songsList.add(song); }
			 */
			Log.i("MyActivity", "Size of songs list" + songsList.size());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return songsList;
	}

	public ArrayList<HashMap<String, String>> sampleSongList(InputStream is) {
		try {

			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			StringBuffer sb = new StringBuffer();
			String eachLine = br.readLine();
			while (eachLine != null) {
				sb.append(eachLine);
				eachLine = br.readLine();
			}
			String readFile = sb.toString();
			JSONObject object = new JSONObject(readFile);
			JSONArray json = object.getJSONArray("data");
			for (int i = 0; i < json.length(); i++) {
				HashMap<String, String> song = new HashMap<String, String>();
				JSONObject songDetails = json.getJSONObject(i);
				String id = songDetails.getString("id");
				String name = songDetails.getString("title");
				String artist = songDetails.getString("artist");
				song.put(TAG_ID, id);
				song.put(TAG_NAME, name);
				song.put(TAG_LABEL_NAME, artist);
				songsList.add(song);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return songsList;
	}

}
