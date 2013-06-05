package com.example.newplayer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
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

	int albumIndex = 0;
	StringBuffer url = new StringBuffer();
	public ArrayList<HashMap<String, String>> songList = new ArrayList<HashMap<String, String>>();

	private static final String TAG_ID = "id";
	private static final String TAG_NAME = "name";
	private static final String SOURCE_URL = "source_url";
	private String albumName = "";
	Button submitButton;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.songlist);
		albumIndex = getIntent().getExtras().getInt("albumIndex");
		albumName = getIntent().getExtras().getString("albumName");
		url = new StringBuffer(getResources().getString(R.string.songsListURL));
		url.append(albumIndex + "/songs.json");
		
		final Thread thread = new Thread() {
			public void run() {
				JSONParser jParser = new JSONParser();

				try {
					String jsonString = jParser.readJsonFromUrl(url.toString());
					JSONObject jsonObject = new JSONObject(jsonString);
					JSONArray songs = jsonObject.getJSONArray("songs");
					for (int i = 0; i < songs.length(); i++) {
						HashMap<String, String> song = new HashMap<String, String>();
						JSONObject songDetails = songs.getJSONObject(i);
						String id = songDetails.getString(TAG_ID);
						String name = songDetails.getString(TAG_NAME);
						// String songUrl = songDetails.getString(SOURCE_URL);
						song.put(TAG_ID, id);
						song.put(TAG_NAME, name);
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
