package com.example.beamto;

import com.example.beamto.R;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ListView;

public class PlayList extends ListActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.playlist);
		NewMediaPlayer instance = NewMediaPlayer.getActivity();
		PlayListAdaptor songListAdapter = new PlayListAdaptor(this,
				instance.getSelectedSongList());
		ListView lv = getListView();
		lv.setAdapter(songListAdapter);

	}

}
