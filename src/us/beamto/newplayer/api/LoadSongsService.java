package us.beamto.newplayer.api;

import org.apache.http.client.HttpClient;

import us.beamto.newplayer.common.VariablesList;
import us.beamto.newplayer.ui.activites.Subscriber;

import android.app.IntentService;
import android.content.Intent;

public class LoadSongsService extends IntentService{

	public LoadSongsService() {
		super("LoadSongsService");
	}

	public void onCreate() {
        super.onCreate();
        System.out.println("Server, >>>onCreate()");
    }

	
	@Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, startId, startId);
        System.out.println("%%%%%%% LocalService , Received start id " + startId + ": " + intent);

        return START_STICKY;
    }
	
	@Override
	protected void onHandleIntent(Intent argument) {
		String albumIndex = (String)argument.getExtras().getString("Album-Index");
		String albumImage = (String)argument.getExtras().getString("Album-Image");
		String []params = {albumIndex,albumImage};
		String []parameters = {VariablesList.ALBUM_JSON_PARAMETER};
		ApiRequestResult res = new LoadSongsRequest().fillParams(parameters,params).doRequest();
		Subscriber.getInstance().message("NOTIFY_SONG_LOAD");
	}

}
