package us.beamto.newplayer.ui.activites;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import us.beamto.newplayer.R;
import us.beamto.newplayer.R.id;
import us.beamto.newplayer.R.layout;
import us.beamto.newplayer.R.string;
import us.beamto.newplayer.api.JSONParser;
import us.beamto.newplayer.api.LoadSongsService;
import us.beamto.newplayer.api.SongsList;
import us.beamto.newplayer.common.BuildValues;
import us.beamto.newplayer.common.PhoneStateChange;
import us.beamto.newplayer.common.VariablesList;
import us.beamto.newplayer.ui.adapters.SongListAdapter;

import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class SongListActivity extends ListActivity implements Observer {

	String albumIndex = "";
	String url = "";
	private static ArrayList<HashMap<String, String>> songList = new ArrayList<HashMap<String, String>>();

	private String albumName = "";
	Button submitButton;
	SongListAdapter songAdapter;
	private ProgressDialog progressDialog;
	ImageView imageView;
	TextView textView;
	TextView artistName;
	TextView totalAlbumSongs;
	NewMediaPlayerActivity instance;
	String albumImageURL;
	String albumUrl;
	String artist;
	String totalSongs;
	String slug;

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_song_list);
		Subscriber.getInstance().addObserver(this);
		
		songList.clear();
		instance = NewMediaPlayerActivity.getActivity();
		albumIndex = getIntent().getExtras().getString("albumIndex");
		albumName = getIntent().getExtras().getString("albumName");
		albumImageURL = getIntent().getExtras().getString("albumImage");
		artist = getIntent().getExtras().getString("artistName");
		totalSongs = getIntent().getExtras().getString("totalSongs");
		slug = getIntent().getExtras().getString("albumSlug");

		url = BuildValues.BASE_URL + VariablesList.SONGS_LIST_URL;
		songAdapter = new SongListAdapter(this, songList, albumName);
		imageView = (ImageView) findViewById(R.id.albumThumbnailImage);
		textView = (TextView) findViewById(R.id.albumName);
		artistName = (TextView) findViewById(R.id.artistName);
		totalAlbumSongs = (TextView) findViewById(R.id.totalSongs);

		textView.setText(albumName);
		artistName.setText(artist);
		totalAlbumSongs.setText(totalSongs + " songs");
		PhoneStateChange listener = new PhoneStateChange();
		TelephonyManager tManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

		if (tManager != null)
			tManager.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);

		imageView.setOnClickListener(new View.OnClickListener() {
			// @Override
			public void onClick(View v) {
				albumUrl = BuildValues.BASE_URL + VariablesList.SONGS_LIST_URL;
				new SongsList(getResources()).execute(albumImageURL, albumUrl,
						albumName, albumIndex);

				finish();
			}
		});

		instance.getImageLoader().displayImage(albumImageURL, imageView);
		ListView lv = getListView();
		lv.setAdapter(songAdapter);
		showDialog(0);
		Intent service = new Intent(this, LoadSongsService.class);
	    service.putExtra("Album-Index", slug);
	    service.putExtra("Album-Image", albumImageURL);
	    this.startService(service);
	
	}

	public static void addToSongsList(HashMap<String, String> song) {
     	songList.add(song);
    }

	@Override
	public void update(Observable observable, Object message) {
		String data = (String) message;
		System.out.println(" Message data: "+ data);
		if (data.equalsIgnoreCase("NOTIFY_SONG_LOAD")) {
			progressDialog.dismiss();
		//	songAdapter.notifyDataSetChanged();
		}
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

/*	private class LoadSongs extends
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
					song.put("AlbumImage", params[2]);
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
	}*/

}
