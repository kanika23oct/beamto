package com.example.newplayer;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.json.*;

public class AlbumList {
	
	String albumUrl ;
	
	public AlbumList(String albumUrl){
		this.albumUrl=albumUrl;
	}
	
	private ArrayList<HashMap<String, String>> albumList = new ArrayList<HashMap<String, String>>();

	public ArrayList<HashMap<String, String>> songList(String url,String parameter,String value) {
		try {
			JSONParser jParser = new JSONParser();
			String jsonString = jParser.readJsonFromUrl(url.toString(),parameter,value);
			JSONObject jsonObject = new JSONObject(jsonString);
			JSONArray songs = jsonObject.getJSONArray(VariablesList.JSON_ALBUM_OBJECT);
			for (int i = 0; i < songs.length(); i++) {
				HashMap<String, String> album = new HashMap<String, String>();
				JSONObject albumDetails = songs.getJSONObject(i);
				String id = albumDetails.getString(VariablesList.TAG_ID);
				String name = albumDetails.getString(VariablesList.TAG_NAME);
				String labelName = albumDetails.getString(VariablesList.TAG_LABEL_NAME);
				String albumImage = albumDetails.getString(VariablesList.TAG_ALBUM_IMAGE);
				String songJsonString = jParser.readJsonFromUrl(albumUrl.toString(),VariablesList.ALBUM_JSON_PARAMETER,id);
				JSONObject songjsonObject = new JSONObject(songJsonString);
				NewMediaPlayer.albumJsonString.put(id+";"+name,songjsonObject.getJSONArray(VariablesList.JSON_SONG_OBJECT));
				album.put(VariablesList.TAG_ID, id);
				album.put(VariablesList.TAG_NAME, name);
				album.put(VariablesList.TAG_LABEL_NAME, labelName);
				album.put(VariablesList.TAG_ALBUM_IMAGE, albumImage);
				albumList.add(album);
			}

		
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

	public static Bitmap LoadImagetoGridView(String url) {
		String image = url;
		Bitmap mIcon11 = null;
		InputStream in;
		try {
			in = new java.net.URL(image).openStream();
			mIcon11 = BitmapFactory.decodeStream(in);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mIcon11;
	}

	

}
