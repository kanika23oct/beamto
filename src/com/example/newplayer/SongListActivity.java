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

public class SongListActivity extends Activity {

	int albumIndex = 0;
	StringBuffer url = new StringBuffer("http://dev.beamto.us/albums/");
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
		url.append(albumIndex + "/songs.json");
		submitButton = (Button ) findViewById(R.id.button);
		
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
						// String songUrl = songDetails.getString(SOURCE_URL);
						song.put(TAG_ID, id);
						song.put(TAG_NAME, name);
						// song.put(SOURCE_URL, songUrl);
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
			Thread.sleep(1500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	String []items = new String[songList.size()];
	for(int i = 0;i<items.length;i++)
	{
		HashMap<String, String> song = songList.get(i);
		items[i] = song.get(TAG_NAME);
		
	}
	/*ListAdapter adapter = new SimpleAdapter(this, songList,
			android.R.layout.simple_list_item_multiple_choice, new String[] { "name" },
				new int[] { R.id.songTitle });*/
	ArrayAdapter<String> adaptor = new ArrayAdapter<String>(this,
			 android.R.layout.simple_list_item_multiple_choice, items); 
	  //	setListAdapter(adapter);

		final ListView lv = (ListView) findViewById(android.R.id.list);
		lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		lv.setAdapter(adaptor);
		final  ArrayList<HashMap<String, String>> selectedSongList = new ArrayList<HashMap<String, String>>();
		submitButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
				 SparseBooleanArray checked = lv.getCheckedItemPositions();
				 for (int i = 0; i < checked.size(); i++) {
					final int position = checked.keyAt(i); 
					System.out.println("Adding Selected Song");
					 Thread t = new Thread() {
							public void run() {
								 HashMap<String, String> song = new HashMap<String, String>();
								JSONParser jParser = new JSONParser();
								try {
									String jsonSongURL = "http://dev.beamto.us/songs/"
											+ songList.get(position).get(TAG_ID) + ".json";
									String jsonString = jParser
											.readJsonFromUrl(jsonSongURL);
									JSONObject jsonObject = new JSONObject(jsonString);
									String songUrl = jsonObject.getString("url");
									System.out.println(songUrl);
									song.put("songUrl", songUrl);
									song.put("songName", songList.get(position).get(TAG_NAME));
									song.put("albumName", albumName);
									selectedSongList.add(song);
									
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
							Thread.sleep(500);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					
				 }
				 Intent in = new Intent(getApplicationContext(),
							NewMediaPlayer.class);
					in.putExtra("SelectedSongList", selectedSongList);
					startActivityForResult(in, 0);
					finish();
			}
		});
		
		
		
	/*	lv.setOnItemClickListener(new OnItemClickListener() {
		
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				song = songList.get(position);
				
				Thread t = new Thread() {
					public void run() {
						
						JSONParser jParser = new JSONParser();
						try {
							String jsonSongURL = "http://dev.beamto.us/songs/"
									+ song.get(TAG_ID) + ".json";
							String jsonString = jParser
									.readJsonFromUrl(jsonSongURL);
							JSONObject jsonObject = new JSONObject(jsonString);
							String songUrl = jsonObject.getString("url");

							Intent in = new Intent(getApplicationContext(),
									NewMediaPlayer.class);
							in.putExtra("songUrl", songUrl);
							in.putExtra("songName", song.get(TAG_NAME));
							in.putExtra("albumName", albumName);
							startActivityForResult(in, 0);
							finish();
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
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		});*/
	}
	
	public void onListItemClick(ListView parent, View v, int position, long id){
        Toast.makeText(this, "You have selected " + songList.get(position).get("songName"), Toast.LENGTH_SHORT).show();
    }

}
