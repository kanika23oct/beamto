package us.beamto.newplayer.ui.activites;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.os.Bundle;
import android.widget.GridView;
import us.beamto.newplayer.R;
import us.beamto.newplayer.ui.adapters.VideoAdaptor;

public class VideoPlayListActivity extends BaseActivity{

	ArrayList<HashMap<String, String>> videoPlayList = new ArrayList<HashMap<String, String>>();
	GridView gridView;
	VideoAdaptor adaptor;
	
	private static Activity activity = null;
	public VideoPlayListActivity() {
		super(R.string.app_name);
	}
	
	public static VideoPlayListActivity getInstance(){
		if(activity == null)
			activity = new VideoPlayListActivity();
		return (VideoPlayListActivity) activity;
	}
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.playlist_grid_layout);
		gridView = (GridView)findViewById(R.id.grid_view_playlist);
		adaptor = new VideoAdaptor(this,videoPlayList);
		gridView.setAdapter(adaptor);
		
	}

}
