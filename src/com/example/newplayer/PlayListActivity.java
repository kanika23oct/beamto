package com.example.newplayer;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

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
	public ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.playlist);
		ArrayList<HashMap<String, String>> songsListData = new ArrayList<HashMap<String, String>>();
		LayoutInflater inflater = getLayoutInflater();
		View convertView  = inflater.inflate(R.layout.playlist_item, null);
		ImageButton albumPlayButton = (ImageButton)convertView.findViewById(R.id.btnAlbumPlay);
		
		AlbumList plm = new AlbumList();
		AssetManager am = this.getAssets();
		InputStream is;
		try {
			is = am.open("beamtoNew.json");
			this.songsList = plm.sampleSongList(is);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (int i = 0; i < songsList.size(); i++) {
			HashMap<String, String> song = songsList.get(i);

			songsListData.add(song);
		}

		ListAdapter adapter = new SimpleAdapter(this, songsListData,
				R.layout.playlist_item, new String[] { "name" },
				new int[] { R.id.albumTitle });

		setListAdapter(adapter);
		
		
		ListView lv = getListView();
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

	    albumPlayButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
			}
		});

	}
	
	
}