package com.example.beamto;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.beamto.R;

import android.content.res.Resources;
import android.os.AsyncTask;
import android.view.View;

public class SongsList extends AsyncTask<String, Void, Boolean> {

	String albumName = "";
	private static Resources resources;
	StringBuffer albumUrl;
	private String imageURL = "";
	private String albumIndex = "";
	NewMediaPlayer instance = null;

	public SongsList(Resources resources) {
		this.resources = resources;
	}

	@Override
	public Boolean doInBackground(String... params) {
		JSONParser jParser = new JSONParser();
		instance = NewMediaPlayer.getActivity();
		boolean playSongs = false;
		try {

			this.imageURL = params[0];
			this.albumUrl = new StringBuffer(params[1]);
			this.albumName = params[2];
			this.albumIndex = params[3];
			JSONArray songs = NewMediaPlayer.albumJsonString.get(albumIndex
					+ ";" + albumName);
			for (int i = 0; i < songs.length(); i++) {
				HashMap<String, String> song = new HashMap<String, String>();
				JSONObject songDetails = songs.getJSONObject(i);
				String id = songDetails.getString(VariablesList.TAG_ID);
				String name = songDetails.getString(VariablesList.TAG_NAME);
				String jsonSongUrl = resources.getString(R.string.songURL);
				String jsonStringSong = jParser.readJsonFromUrl(
						jsonSongUrl.toString(), VariablesList.TAG_ID, id);
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
				// NewMediaPlayer.selectedSongs.add(song);
				instance.addToSelectedList(song);
			}

			if (instance.getSelectedSongList().size() > 0)
				playSongs = true;

		} catch (JSONException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return playSongs;
	}

	@Override
	public void onPostExecute(Boolean result) {

		if (result == true) {
			if (!NewMediaPlayer.mediaPlayer.isPlaying()) {
				HashMap<String, String> playingSong = instance
						.getSelectedSong(0);
				if (playingSong != null) {
					String url = playingSong.get("songUrl");
					String songName = playingSong.get("songName");
					if (songName != null) {
						instance.setCurrentSongName(albumName + "-"+ songName);
					}
					instance.slidingDrawer.open();
					instance.playSong(url);
					

				}
			}
		}

	}

}
