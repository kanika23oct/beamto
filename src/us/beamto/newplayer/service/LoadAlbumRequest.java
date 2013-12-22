package us.beamto.newplayer.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import org.apache.http.client.HttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;

import us.beamto.newplayer.api.ApiRequestResult;
import us.beamto.newplayer.api.BaseApiRequest;
import us.beamto.newplayer.api.JSONParser;
import us.beamto.newplayer.api.PostApiRequest;
import us.beamto.newplayer.common.BuildValues;
import us.beamto.newplayer.common.VariablesList;
import us.beamto.newplayer.ui.activites.NewMediaPlayerActivity;
import us.beamto.newplayer.ui.activites.Subscriber;
import us.beamto.newplayer.ui.adapters.ClickableListAdapter;

public class LoadAlbumRequest extends PostApiRequest implements Observer {

	String totalSongs = "";
	Context context;
	ArrayList<HashMap<String, String>> albumList = new ArrayList<HashMap<String, String>>();

	public LoadAlbumRequest(HttpClient httpClient) {
		super(httpClient);
		Subscriber.getInstance().addObserver(this);

	}

	public LoadAlbumRequest(Context context) {
		super();
		Subscriber.getInstance().addObserver(this);
		this.context = context;
	}
	
	 public BaseApiRequest fillParams(String []parameter,String []params) {
	        resetParams();

	        addParam(parameter[0], params[0]);
	        return this;
	    }
	 

	@Override
	protected String getUrl() {
		return BuildValues.BASE_URL + VariablesList.ALBUMS_URL;
	}

	@Override
	public void update(Observable observable, Object data) {
		String message = (String) data;
		if (message.contains("TOTAL_SONGS")) {
			totalSongs = message.split(";")[1];
		}
	}

	@Override
	protected void parseJsonResponse(ApiRequestResult requestResult,
			JSONObject albumResultObject) throws JSONException {
		super.parseJsonResponse(requestResult, albumResultObject);
		NewMediaPlayerActivity instance = NewMediaPlayerActivity.getActivity();
		JSONParser jParser = new JSONParser();
		if (requestResult.isStatusSuccessful()) {
			try {
				JSONArray albums = albumResultObject
						.getJSONArray(VariablesList.JSON_ALBUM_OBJECT);
				for (int i = 0; i < albums.length(); i++) {
					HashMap<String, String> album = new HashMap<String, String>();
					JSONObject albumDetails = albums.getJSONObject(i);
					String id = albumDetails.getString(VariablesList.TAG_ID);
					String slug = albumDetails
							.getString(VariablesList.TAG_SLUG);
					String name = albumDetails
							.getString(VariablesList.TAG_NAME);
					String labelName = albumDetails
							.getString(VariablesList.TAG_LABEL_NAME);
					String albumImage = albumDetails
							.getString(VariablesList.TAG_ALBUM_IMAGE);
					String artistName = albumDetails
							.getString(VariablesList.TAG_ARTIST_NAME);
					String songJsonString = jParser
							.readJsonFromUrl(BuildValues.BASE_URL
									+ VariablesList.SONGS_LIST_URL,
									VariablesList.ALBUM_JSON_PARAMETER, slug);
					JSONObject songjsonObject = new JSONObject(songJsonString);
					NewMediaPlayerActivity
							.getActivity()
							.setAlbumJsonArray(
									id + ";" + name,
									songjsonObject
											.getJSONArray(VariablesList.JSON_SONG_OBJECT));

					album.put(VariablesList.TAG_ID, id);
					album.put(VariablesList.TAG_SLUG, slug);
					album.put(VariablesList.TAG_NAME, name);
					album.put(VariablesList.TAG_LABEL_NAME, labelName);
					album.put(VariablesList.TAG_ALBUM_IMAGE, albumImage);
					album.put(VariablesList.TAG_ARTIST_NAME, artistName);
					String size = ""
							+ songjsonObject.getJSONArray(
									VariablesList.JSON_SONG_OBJECT).length();
					album.put(VariablesList.NUMBER_OF_SONGS, size);
					albumList.add(album);

				}
               
				System.out.println("Album Size :"+albumList.size());
				if (albumList.size() == 0) {
					instance.setLastPage(true);
					instance.setLoading(false);

				} else {
					instance.setNewList(albumList);
					instance.setLoading(false);
					instance.appendToAdaptor();
					
				}
				
			//	ClickableListAdapter adapter = instance.getClickableListAdapter();
			//	adapter.notifyDataSetChanged();
			} catch (Exception e) {
				requestResult.resultObject = "";
			}
		}
	}

	protected void addTotalSongs() {

	}

}
