package com.example.newplayer;

import java.io.IOException;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.res.Resources;

public class SongsList implements Runnable {

	String albumName = "";
	private static Resources resources;
	StringBuffer albumUrl;
	private String imageURL = "";
	private String albumIndex = "";

	public SongsList(String imageURL, StringBuffer albumURL, String albumName,String albumIndex,Resources resources) {
		this.imageURL = imageURL;
		this.albumUrl = albumURL;
		this.albumName = albumName;
		this.albumIndex = albumIndex;
		this.resources = resources;
	}

	@Override
	public void run() {
		JSONParser jParser = new JSONParser();
		try {
			JSONArray songs = NewMediaPlayer.albumJsonString.get(albumIndex+";"+albumName);
			for (int i = 0; i < songs.length(); i++) {
				HashMap<String, String> song = new HashMap<String, String>();
				JSONObject songDetails = songs.getJSONObject(i);
				String id = songDetails.getString(VariablesList.TAG_ID);
				String name = songDetails.getString(VariablesList.TAG_NAME);
				String jsonSongUrl = resources.getString(R.string.songURL);
				String jsonStringSong = jParser.readJsonFromUrl(jsonSongUrl.toString(),VariablesList.TAG_ID,id);
				JSONObject jsonObjectSong = new JSONObject(jsonStringSong);
				String songUrl = jsonObjectSong.getString("url");

				if (songUrl.startsWith("https://")) {
					songUrl = songUrl.replaceFirst("https://", "http://");
				}
				System.out.println(songUrl);
				song.put(VariablesList.SONG_URL_PARAMETER, songUrl);
				song.put(VariablesList.TAG_ID, id);
				song.put(VariablesList.SONG_NAME_PARAMETER, name);
				song.put(VariablesList.ALBUM_NAME_PARAMETER, albumName);
				song.put(VariablesList.TAG_ALBUM_IMAGE, imageURL);
				NewMediaPlayer.selectedSongs.add(song);
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
