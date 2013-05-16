package com.example.newplayer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import org.json.JSONArray;
import org.json.JSONObject;

public class SongListActivity extends ListActivity {

	int albumIndex = 0;
	StringBuffer url = new StringBuffer("http://dev.beamto.us/albums/");
	public ArrayList<HashMap<String, String>> songList = new ArrayList<HashMap<String, String>>();
	private static final String TAG_ID = "id";
	private static final String TAG_NAME = "name";
	private static final String SOURCE_URL = "source_url";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.songlist);
		albumIndex = getIntent().getExtras().getInt("albumIndex");
		url.append(albumIndex + "/songs.json");
		// new SongList().execute(url.toString());

		Thread t = new Thread() {
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
						String songUrl = songDetails.getString(SOURCE_URL);
						song.put(TAG_ID, id);
						song.put(TAG_NAME, name);
						song.put(SOURCE_URL, songUrl);
						songList.add(song);
						System.out.println("Test");
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
		t.start();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.i("SongListActivity", "Size of songs list" + songList.size());
		ListAdapter adapter = new SimpleAdapter(this, songList,
				R.layout.songlist_item, new String[] { "name" },
				new int[] { R.id.songTitle });

		setListAdapter(adapter);

		ListView lv = getListView();
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				HashMap<String, String> song = songList.get(position);
				String songUrl = song.get(SOURCE_URL);
				Intent in = new Intent(getApplicationContext(),
						NewMediaPlayer.class);
				in.putExtra("songUrl", songUrl);
				startActivityForResult(in, 0);
			}
		});
	}

	
}
