package com.example.beamto;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.newplayer.R;

import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

public class SongListActivity extends ListActivity {

	String albumIndex = "";
	String url = "";
	public ArrayList<HashMap<String, String>> songList = new ArrayList<HashMap<String, String>>();

	private String albumName = "";
	private String albumImageURL = "";
	Button submitButton;
	SongListAdapter songAdapter;
	private ProgressDialog progressDialog;

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.songlist);
		albumIndex = getIntent().getExtras().getString("albumIndex");
		albumName = getIntent().getExtras().getString("albumName");
		albumImageURL = getIntent().getExtras().getString("AlbumImage");
		url = getResources().getString(R.string.songsListURL);
		songAdapter = new SongListAdapter(this, songList,
				albumName);
		ListView lv = getListView();
		lv.setAdapter(songAdapter);
		showDialog(0);
      new LoadSongs().execute(url,albumIndex);

	}

	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case 0: // Spinner
			progressDialog = new ProgressDialog(this);
			progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progressDialog.setMessage("Loading...Please wait");
			progressDialog.setCancelable(true);
			return progressDialog;
		default:
			return null;
		}
	}
	private class LoadSongs extends
			AsyncTask<String, Void, ArrayList<HashMap<String, String>>> {

		@Override
		protected ArrayList<HashMap<String, String>> doInBackground(
				String... params) {
			String url = params[0];
			String albumIndex = params[1];
			JSONParser jParser = new JSONParser();
			try {
				String jsonString = jParser.readJsonFromUrl(url,
						VariablesList.ALBUM_JSON_PARAMETER, albumIndex);
				JSONObject jsonObject = new JSONObject(jsonString);
				JSONArray songs = jsonObject
						.getJSONArray(VariablesList.JSON_SONG_OBJECT);
				for (int i = 0; i < songs.length(); i++) {
					HashMap<String, String> song = new HashMap<String, String>();
					JSONObject songDetails = songs.getJSONObject(i);
					String id = songDetails.getString(VariablesList.TAG_ID);
					String name = songDetails.getString(VariablesList.TAG_NAME);
					song.put(VariablesList.TAG_ID, id);
					song.put(VariablesList.TAG_NAME, name);
					song.put("AlbumImage", albumImageURL);
					songList.add(song);
				}

			} catch (JSONException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			return songList;
		}

		@Override
		public void onPostExecute(ArrayList<HashMap<String, String>> result) {
			progressDialog.dismiss();
			songAdapter.notifyDataSetChanged();
		}
	}
}
