package us.beamto.newplayer.service;

import android.app.IntentService;
import android.content.Intent;

public class GetPlaylistsService extends IntentService{

	public GetPlaylistsService(String name) {
		super("GetPlaylistService");
	}
	
	public void onCreate(){
		super.onCreate();
		System.out.println("Server, >> onCreate");
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, startId, startId);
		System.out.println("%%%%%%% LocalService , Received start id "
				+ startId + ": " + intent);

		return START_STICKY;
	}

	@Override
	protected void onHandleIntent(Intent arg0) {
		
	}

}
