package us.beamto.newplayer.ui.adapters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import us.beamto.newplayer.R;
import us.beamto.newplayer.R.id;
import us.beamto.newplayer.R.layout;
import us.beamto.newplayer.R.string;
import us.beamto.newplayer.api.JSONParser;
import us.beamto.newplayer.common.BuildValues;
import us.beamto.newplayer.common.VariablesList;
import us.beamto.newplayer.ui.activites.NewMediaPlayerActivity;
import us.beamto.newplayer.ui.activites.Subscriber;



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
	NewMediaPlayerActivity instance;

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
		instance = NewMediaPlayerActivity.getActivity();
		View v = null;
		if (convertView != null)
			v = convertView;
		else
			v = inflater.inflate(R.layout.li_song_list, parent, false);

		TextView itemSongName = (TextView) v.findViewById(R.id.songTitle);
		itemSongName.setText(songs.get(VariablesList.TAG_NAME));
		itemSongName.setOnClickListener(new View.OnClickListener() {
		
			@Override
			public void onClick(View view) {
				 String jsonSongURL = BuildValues.BASE_URL + VariablesList.SONG_URL;
               	new PlaySong().execute(jsonSongURL,songsList.get(position).get(VariablesList.TAG_ID),songsList.get(position).get(VariablesList.TAG_NAME),songsList.get(position).get("AlbumImage") );

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
				song.put("coverart_small", params[3]);
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
			//	HashMap<String, String> playingSong = result;
					String url = result.get("songUrl");
					instance.setCurrentIndex(instance.getSelectedSongList().size()-1);
					String songName = result.get("songName");
					Subscriber.getInstance().message("SET_TITLE;"+albumName + "-"+ songName);
					instance.playSong(url);
					instance.slidingDrawer.open();
					((Activity)context).finish();
			
		}

	}

}
