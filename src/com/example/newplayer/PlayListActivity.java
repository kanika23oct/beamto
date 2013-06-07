package com.example.newplayer;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class PlayListActivity extends ListActivity {
	public  ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();

	
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.playlist);
		ArrayList<HashMap<String, String>> songsListData = new ArrayList<HashMap<String, String>>();
		
		
		final AssetManager am = this.getAssets();
		InputStream is;
		final Thread threadAlbums = new Thread() {
			public void run() {
			/*	String url = getResources().getString(R.string.albumsURL);
				songsList = new AlbumList().songList(url);*/
				
				try {
					songsList = new AlbumList().sampleSongList(am.open("beamtoNew.json"));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				synchronized (this) {
					this.notifyAll();
				}
			}
		};
		synchronized (threadAlbums) {
			threadAlbums.start();
			try {
				threadAlbums.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	

		for (int i = 0; i < songsList.size(); i++) {
			HashMap<String, String> song = songsList.get(i);

			songsListData.add(song);
		}

		ClickableListAdapter adapterClickable = new ClickableListAdapter(this, songsList);
		
		
		
		ListView lv = getListView();
		lv.setAdapter( adapterClickable);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				HashMap<String, String> song = songsList.get(position);
				int songIndex = Integer.parseInt(song.get("id"));
				String albumName = (song.get("name"));
				Intent in = new Intent(getApplicationContext(),
						SongListActivity.class);
				in.putExtra("albumIndex", songIndex);
				in.putExtra("albumName", albumName);
				startActivityForResult(in, 0);
			}
		});

	    

	}
	
	
}