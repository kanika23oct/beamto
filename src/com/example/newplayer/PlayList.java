package com.example.newplayer;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ListView;

public class PlayList extends ListActivity{
	
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.playlist);
		System.out.println("***** Hello");
		PlayListAdaptor songListAdapter = new PlayListAdaptor(this, NewMediaPlayer.selectedSongs);
		ListView lv = getListView();
		lv.setAdapter(songListAdapter);               
		
	}

}
