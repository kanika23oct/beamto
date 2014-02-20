package us.beamto.newplayer.service;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.client.HttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import us.beamto.newplayer.api.ApiRequestResult;
import us.beamto.newplayer.api.BaseApiRequest;
import us.beamto.newplayer.api.JSONParser;
import us.beamto.newplayer.api.PostApiRequest;
import us.beamto.newplayer.common.BuildValues;
import us.beamto.newplayer.common.VariablesList;
import us.beamto.newplayer.ui.activites.Subscriber;

public class GetPlaylistsRequest extends PostApiRequest {

	Context context;
	ArrayList<HashMap<String, String>> playList = new ArrayList<HashMap<String, String>>();

	public GetPlaylistsRequest(HttpClient httpClient) {
		super(httpClient);
	}

	public GetPlaylistsRequest(Context context) {
		super();
		this.context = context;
	}

	public BaseApiRequest fillParams(String[] parameter, String[] params) {
		resetParams();

		addParam(parameter[0], params[0]);
		return this;
	}

	@Override
	protected String getUrl() {
		return BuildValues.BASE_URL + VariablesList.PLAY_LIST_URL;
	}

	@Override
	protected void parseJsonResponse(ApiRequestResult requestResult,
			JSONObject playlistResultObject) throws JSONException {
		super.parseJsonResponse(requestResult, playlistResultObject);
		JSONParser jsonParser = new JSONParser();
		if(requestResult.isStatusSuccessful()){
			try{
				JSONArray videos = playlistResultObject.getJSONArray(VariablesList.JSON_PLAYLISTS_OBJECT);
				for(int i =0;i<videos.length();i++)
				{
					HashMap<String, String> video = new HashMap<String, String>();
					JSONObject videoDetails = videos.getJSONObject(i);
					String name = videoDetails.getString(VariablesList.TAG_NAME);
					String id = videoDetails.getString(VariablesList.TAG_ID);
					String imageURL = videoDetails.getString(VariablesList.TAG_ALBUM_IMAGE);
					
				}
				
			} catch(Exception e){
				
			}
		}
		

	}
}
