package us.beamto.newplayer.service;

import us.beamto.newplayer.api.ApiRequestResult;
import us.beamto.newplayer.common.VariablesList;
import us.beamto.newplayer.ui.activites.NewMediaPlayerActivity;
import us.beamto.newplayer.ui.activites.Subscriber;
import us.beamto.newplayer.ui.adapters.ClickableListAdapter;
import android.app.IntentService;
import android.content.Intent;

public class LoadAlbumService extends IntentService {

	public LoadAlbumService() {
		super("LoadAlbumService");
	}

	public void onCreate() {
		super.onCreate();
		System.out.println("Server, >>>onCreate()");
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, startId, startId);
		System.out.println("%%%%%%% LocalService , Received start id "
				+ startId + ": " + intent);

		return START_STICKY;
	}

	@Override
	protected void onHandleIntent(Intent argument) {
		String pageNumber = (String) argument.getExtras().getString(
				"Page-Number");
		String[] params = { pageNumber };
		String[] parameters = { VariablesList.JSON_PAGE_OBJECT };
		ApiRequestResult res = new LoadAlbumRequest(this).fillParams(
				parameters, params).doRequest();
		
		// send update
		Intent intentUpdate = new Intent();
		intentUpdate.setAction("us.beamto.newplayer.service.RESPONSE");
		intentUpdate.addCategory(Intent.CATEGORY_DEFAULT);
		sendBroadcast(intentUpdate);

	}

}
