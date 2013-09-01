package us.beamto.newplayer.ui.activites;

import us.beamto.newplayer.R;
import us.beamto.newplayer.R.layout;
import us.beamto.newplayer.common.PhoneStateChange;
import us.beamto.newplayer.ui.adapters.PlayListAdaptor;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.widget.ListView;

public class PlayListActivity extends ListActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_play_list);
		NewMediaPlayerActivity instance = NewMediaPlayerActivity.getActivity();
		PhoneStateChange listener = new PhoneStateChange();
		TelephonyManager tManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if(tManager != null)
            tManager.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
        
		PlayListAdaptor songListAdapter = new PlayListAdaptor(this,
				instance.getSelectedSongList());
		ListView lv = getListView();
		lv.setAdapter(songListAdapter);

	}

}
