package us.beamto.newplayer.common;

import us.beamto.newplayer.ui.activites.NewMediaPlayerActivity;
import us.beamto.newplayer.ui.activites.VideoPlayListActivity;
import us.beamto.newplayer.ui.adapters.ClickableListAdapter;
import us.beamto.newplayer.ui.adapters.VideoAdaptor;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class GetPlaylistBroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if (action
				.equalsIgnoreCase("us.beamto.newplayer.service.GetPlaylistsService")) {
			System.out.println("**** Broadcasting the change of the playlist *****");
			VideoAdaptor adapter = VideoPlayListActivity.getInstance()
					.getVideoAdaptor();
			adapter.notifyDataSetChanged();
		}
	}

}
