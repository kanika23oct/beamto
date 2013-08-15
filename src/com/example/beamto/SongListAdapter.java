package com.example.beamto;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.beamto.R;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

public class SongListAdapter extends BaseAdapter {
	ArrayList<HashMap<String, String>> songsList;
	private Context context;
	private LayoutInflater inflater;
	private String albumName;
	private static Resources resources;

	public SongListAdapter(Context context,
			ArrayList<HashMap<String, String>> songList, String albumName) {
		inflater = LayoutInflater.from(context);
		this.context = context;
		this.songsList = songList;
		this.albumName = albumName;
		resources = context.getResources();
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
			v = inflater.inflate(R.layout.songlist_item, parent, false);

		TextView itemSongName = (TextView) v.findViewById(R.id.songTitle);
		itemSongName.setText(songs.get(VariablesList.TAG_NAME));
		ImageButton songPlayButton = (ImageButton) v
				.findViewById(R.id.btnSongPlay);
		songPlayButton.setOnClickListener(new View.OnClickListener() {
			final HashMap<String, String> song = new HashMap<String, String>();

			@Override
			public void onClick(View view) {

				Thread t = new Thread() {
					public void run() {

						JSONParser jParser = new JSONParser();
						try {
							String jsonSongURL = resources
									.getString(R.string.songURL);
							String jsonString = jParser.readJsonFromUrl(
									jsonSongURL, VariablesList.TAG_ID,
									songs.get(VariablesList.TAG_ID));
							JSONObject jsonObject = new JSONObject(jsonString);
							String songUrl = jsonObject.getString("url");
							System.out.println(songUrl);
							song.put("songUrl", songUrl);
							song.put("songName",
									songs.get(VariablesList.TAG_NAME));
							song.put("albumName", albumName);
							song.put("coverart_small", songs.get("AlbumImage"));
							synchronized (this) {
								this.notifyAll();
							}
						} catch (JSONException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				};
				t.start();
				try {
					synchronized (t) {
						t.wait();
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				NewMediaPlayer.selectedSongs.add(song);
				if (!NewMediaPlayer.mediaPlayer.isPlaying()) {
					HashMap<String, String> playingSong = song;
					if (playingSong != null) {
						String url = playingSong.get("songUrl");
						String songName = playingSong.get("songName");
						if (songName != null) {
							NewMediaPlayer.songTitleLabel.setText(albumName
									+ " - " + songName);
							NewMediaPlayer.songTitle.setText(albumName + " - "
									+ songName);
						}
						NewMediaPlayer.playSong(url);
					}
				}
			}

		});

		return v;
	}

}
