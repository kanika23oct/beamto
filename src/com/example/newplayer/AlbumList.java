package com.example.newplayer;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Log;

import org.json.*;

public class AlbumList {
	private static final String TAG_ID = "id";
	private static final String TAG_NAME = "name";
	private static final String TAG_LABEL_NAME = "label_name";
	private static final String TAG_ALBUM_IMAGE = "coverart_small";

	private ArrayList<HashMap<String, String>> albumList = new ArrayList<HashMap<String, String>>();

	public ArrayList<HashMap<String, String>> songList(String url) {
		try {
			JSONParser jParser = new JSONParser();
			String jsonString = jParser.readJsonFromUrl(url.toString());
			JSONObject jsonObject = new JSONObject(jsonString);
			JSONArray songs = jsonObject.getJSONArray("data");
			for (int i = 0; i < songs.length(); i++) {
				HashMap<String, String> album = new HashMap<String, String>();
				JSONObject albumDetails = songs.getJSONObject(i);
				String id = albumDetails.getString(TAG_ID);
				String name = albumDetails.getString(TAG_NAME);
				String labelName = albumDetails.getString(TAG_LABEL_NAME);
				String albumImage = albumDetails.getString(TAG_ALBUM_IMAGE);
				album.put(TAG_ID, id);
				album.put(TAG_NAME, name);
				album.put(TAG_LABEL_NAME, labelName);
				album.put(TAG_ALBUM_IMAGE, albumImage);
				albumList.add(album);
			}

			Log.i("AlbumListActivity", "Size of album list " + albumList.size());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return albumList;
	}
	
	public static void LoadImageFromWebOperations(String url) {
		 final String image = url;
		final Thread startAlbum = new Thread() {
			public void run() {
				InputStream in;
				try {
				
					in = new java.net.URL(image).openStream();
					final Bitmap mIcon11 = BitmapFactory.decodeStream(in);
					NewMediaPlayer.artistImage.post(new Runnable() {
			                public void run() {
			                	NewMediaPlayer.artistImage.setImageBitmap(mIcon11);
			                }
		            });

					synchronized (this) {
						this.notifyAll();
					}
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};

		synchronized (startAlbum) {
			startAlbum.start();
			try {
				startAlbum.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
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
				HashMap<String, String> album = new HashMap<String, String>();
				JSONObject albumDetails = json.getJSONObject(i);

				/*String id = songDetails.getString("id");
				String name = songDetails.getString("title");
				String artist = songDetails.getString("artist");
				song.put("id", id);
				song.put(TAG_NAME, name);
				song.put(TAG_LABEL_NAME, artist);
				songsList.add(song);*/

				String id = albumDetails.getString(TAG_ID);
				String name = albumDetails.getString(TAG_NAME);
				String labelName = albumDetails.getString(TAG_LABEL_NAME);
				String albumImage = albumDetails.getString(TAG_ALBUM_IMAGE);
				album.put(TAG_ID, id);
				album.put(TAG_NAME, name);
				album.put(TAG_LABEL_NAME, labelName);
				album.put(TAG_ALBUM_IMAGE, albumImage);
				albumList.add(album);

			}
			Log.v("Play List", "Size of songs list" + albumList.size());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return albumList;
	}

}
