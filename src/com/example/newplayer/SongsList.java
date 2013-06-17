package com.example.newplayer;

import java.io.IOException;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.res.Resources;

public class SongsList implements Runnable {

	private static final String TAG_ID = "id";
	private static final String TAG_NAME = "name";
	private static final String SOURCE_URL = "source_url";
	private static final String TAG_ALBUM_IMAGE = "coverart_small";
	String albumName = "";
	private static Resources resources;
	StringBuffer albumUrl;
	private String imageURL = "";

	public SongsList(String imageURL,StringBuffer albumURL,String albumName){
		this.imageURL = imageURL;
		this.albumUrl = albumURL;
		this.albumName = albumName;
	}
	@Override
	public void run() {
		JSONParser jParser = new JSONParser();
		try {
			String jsonString = jParser.readJsonFromUrl(albumUrl.toString());
			JSONObject jsonObject = new JSONObject(jsonString);
			JSONArray songs = jsonObject.getJSONArray("songs");
			for (int i = 0; i < songs.length(); i++) {
				HashMap<String, String> song = new HashMap<String, String>();
				JSONObject songDetails = songs.getJSONObject(i);
				String id = songDetails.getString(TAG_ID);
				String name = songDetails.getString(TAG_NAME);
				String jsonSongURL = "http://dev.beamto.us/songs/" + id
						+ ".json";
				String jsonStringSong = jParser.readJsonFromUrl(jsonSongURL);
				JSONObject jsonObjectSong = new JSONObject(jsonStringSong);
				String songUrl = jsonObjectSong.getString("url");

				if (songUrl.startsWith("https://")) {
					songUrl = songUrl.replaceFirst("https://", "http://");
				}
				System.out.println(songUrl);
				song.put("songUrl", songUrl);
				song.put(TAG_ID, id);
				song.put("songName", name);
				song.put("albumName", albumName);
				song.put(TAG_ALBUM_IMAGE, imageURL);
				NewMediaPlayer.selectedSongs.add(song);
				System.out.println("Adding songs");
				synchronized (this) {
				this.notifyAll();
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
