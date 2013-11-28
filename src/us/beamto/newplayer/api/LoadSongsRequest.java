package us.beamto.newplayer.api;

import org.apache.http.client.HttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import us.beamto.newplayer.common.BuildValues;
import us.beamto.newplayer.common.VariablesList;
import us.beamto.newplayer.ui.activites.SongListActivity;
import us.beamto.newplayer.ui.activites.Subscriber;

import java.util.HashMap;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class LoadSongsRequest extends PostApiRequest implements Observer{

	String loadAlbumURL = "";
	
    public LoadSongsRequest(HttpClient httpClient) {
    	super(httpClient);
    	Subscriber.getInstance().addObserver(this);
		
	}
    
    public LoadSongsRequest() {
		super();
		Subscriber.getInstance().addObserver(this);
		
	}
    

	public String getUrl() {
        return BuildValues.BASE_URL + VariablesList.SONGS_LIST_URL;
    }

    public BaseApiRequest fillParams(String []parameter,String []params) {
        resetParams();

        addParam(parameter[0], params[0]);
        loadAlbumURL = params[1];
        return this;
    }


    @Override
    protected void parseJsonResponse(ApiRequestResult requestResult, JSONObject songResultObject) throws JSONException {
        super.parseJsonResponse(requestResult, songResultObject);
        if (requestResult.isStatusSuccessful()) {
            try {
            	JSONArray songs = songResultObject
						.getJSONArray(VariablesList.JSON_SONG_OBJECT);
            	for (int i = 0; i < songs.length(); i++) {
					HashMap<String, String> song = new HashMap<String, String>();
					JSONObject songDetails = songs.getJSONObject(i);
					String id = songDetails.getString(VariablesList.TAG_ID);
					String name = songDetails.getString(VariablesList.TAG_NAME);
					song.put(VariablesList.TAG_ID, id);
					song.put(VariablesList.TAG_NAME, name);
					song.put("AlbumImage", loadAlbumURL);
					SongListActivity.addToSongsList(song);
				}
				
		     } catch (Exception e) {
                requestResult.resultObject = "";
            }
        }
    }

	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		
	}
}