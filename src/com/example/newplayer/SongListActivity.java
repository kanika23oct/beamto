package com.example.newplayer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import org.json.JSONArray;
import org.json.JSONObject;

public class SongListActivity extends ListActivity {

	String albumIndex = "";
	String url = "";
	public ArrayList<HashMap<String, String>> songList = new ArrayList<HashMap<String, String>>();

	private String albumName = "";
	private String albumImageURL = "";
	Button submitButton;
	private String parameter = "album_id";

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.songlist);
		albumIndex = getIntent().getExtras().getString("albumIndex");
		albumName = getIntent().getExtras().getString("albumName");
		albumImageURL = getIntent().getExtras().getString("AlbumImage");
		url = getResources().getString(R.string.songsListURL);
		//url.append(albumIndex + "/songs.json");
		
		final Thread thread = new Thread() {
			public void run() {
				JSONParser jParser = new JSONParser();

				try {
					String jsonString = jParser.readJsonFromUrl(url,parameter,albumIndex);
					JSONObject jsonObject = new JSONObject(jsonString);
					JSONArray songs = jsonObject.getJSONArray("songs");
					for (int i = 0; i < songs.length(); i++) {
						HashMap<String, String> song = new HashMap<String, String>();
						JSONObject songDetails = songs.getJSONObject(i);
						String id = songDetails.getString(VariablesList.TAG_ID);
						String name = songDetails.getString(VariablesList.TAG_NAME);
						// String songUrl = songDetails.getString(SOURCE_URL);
						song.put(VariablesList.TAG_ID, id);
						song.put(VariablesList.TAG_NAME, name);
						song.put("AlbumImage", albumImageURL);
						// song.put(SOURCE_URL, songUrl);
						songList.add(song);
						System.out.println("Test");
					}
					synchronized (this) {
						this.notifyAll();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		};
		synchronized (thread) {
			thread.start();
			try {
				thread.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		SongListAdapter songAdapter = new SongListAdapter(this, songList,
				albumName);
		ListView lv = getListView();
		lv.setAdapter(songAdapter);

	}

}
