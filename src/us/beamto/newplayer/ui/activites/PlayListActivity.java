package us.beamto.newplayer.ui.activites;

import us.beamto.newplayer.R;
import us.beamto.newplayer.R.layout;
import us.beamto.newplayer.ui.adapters.PlayListAdaptor;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ListView;

public class PlayListActivity extends ListActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_play_list);
		NewMediaPlayerActivity instance = NewMediaPlayerActivity.getActivity();
		PlayListAdaptor songListAdapter = new PlayListAdaptor(this,
				instance.getSelectedSongList());
		ListView lv = getListView();
		lv.setAdapter(songListAdapter);

	}

}
