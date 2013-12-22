package us.beamto.newplayer.common;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import us.beamto.newplayer.ui.activites.NewMediaPlayerActivity;
import us.beamto.newplayer.ui.adapters.ClickableListAdapter;

public class LoadAlbumBroadcastReceiver extends BroadcastReceiver {

	@Override
	  public void onReceive(Context context, Intent intent) {
		ClickableListAdapter adapter = NewMediaPlayerActivity.getActivity().getClickableListAdapter();
		adapter.notifyDataSetChanged();
	  }
	
}
