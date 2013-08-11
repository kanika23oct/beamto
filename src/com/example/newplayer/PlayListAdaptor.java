package com.example.newplayer;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class PlayListAdaptor extends BaseAdapter {

	ArrayList<HashMap<String, String>> songsList;
	private Context context;
	private LayoutInflater inflater;

	public PlayListAdaptor(Context context,
			ArrayList<HashMap<String, String>> songList) {
		inflater = LayoutInflater.from(context);
		this.context = context;
		this.songsList = songList;
	}

	@Override
	public int getCount() {
		return songsList.size();
	}

	@Override
	public Object getItem(int position) {
		return songsList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final HashMap<String, String> songs = songsList.get(position);
		View v = null;
		if (convertView != null)
			v = convertView;
		else
			v = inflater.inflate(R.layout.playlist_item, parent, false);
		
		final String songName = songs.get(VariablesList.SONG_NAME_PARAMETER);
		
		TextView itemSongName = (TextView) v.findViewById(R.id.albumTitle);
		itemSongName.setText(songName);
		itemSongName.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String albumName = songs.get(VariablesList.ALBUM_NAME_PARAMETER);
				String url = songs.get(VariablesList.SONG_URL_PARAMETER);
				NewMediaPlayer.playSong(url);
				NewMediaPlayer.songTitleLabel.setText(albumName
						+ " - " + songName);
				 NewMediaPlayer.songTitle.setText(albumName+"-"+songName);
			}
		});
		return v;
	}

}
