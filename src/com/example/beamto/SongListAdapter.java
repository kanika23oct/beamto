package com.example.beamto;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.beamto.R;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
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
	HashMap<String, String> songs;
	NewMediaPlayer instance;

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
	public View getView(final int position, View convertView, ViewGroup parent) {
		songs = songsList.get(position);
		instance = NewMediaPlayer.getActivity();
		View v = null;
		if (convertView != null)
			v = convertView;
		else
			v = inflater.inflate(R.layout.songlist_item, parent, false);

		TextView itemSongName = (TextView) v.findViewById(R.id.songTitle);
		itemSongName.setText(songs.get(VariablesList.TAG_NAME));
		itemSongName.setOnClickListener(new View.OnClickListener() {
		
			@Override
			public void onClick(View view) {
               String jsonSongURL = resources.getString(R.string.songURL);
               	new PlaySong().execute(jsonSongURL,songsList.get(position).get(VariablesList.TAG_ID),songsList.get(position).get(VariablesList.TAG_NAME) );

			}

		});

		return v;
	}

	private class PlaySong extends
			AsyncTask<String, Void, HashMap<String, String>> {

		final HashMap<String, String> song = new HashMap<String, String>();

		@Override
		protected HashMap<String, String> doInBackground(String... params) {
			JSONParser jParser = new JSONParser();
			try {
				String jsonString = jParser.readJsonFromUrl(params[0],
						VariablesList.TAG_ID, params[1]);
				JSONObject jsonObject = new JSONObject(jsonString);
				String songUrl = jsonObject.getString("url");
				System.out.println(songUrl);
				if (songUrl.startsWith("https://")) {
					songUrl = songUrl.replaceFirst("https://", "http://");
				}
				song.put("songUrl", songUrl);
				song.put("songName", params[2]);
				song.put("albumName", albumName);
				song.put("coverart_small", songs.get("AlbumImage"));
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return song;

		}

		@Override
		public void onPostExecute(HashMap<String, String> result) {

			instance.addToSelectedList(result);
			// NewMediaPlayer.selectedSongs.add(song);
			if (!NewMediaPlayer.mediaPlayer.isPlaying()) {
			//	HashMap<String, String> playingSong = result;
				if (result != null) {
					String url = result.get("songUrl");
					
					String songName = result.get("songName");
					if (songName != null) {
						instance.setCurrentSongName(albumName + "-"+ songName);
					}
					instance.slidingDrawer.open();
					instance.playSong(url);
					((Activity)context).finish();
				}
			}
		}

	}

}
