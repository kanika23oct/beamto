package us.beamto.newplayer.api;

import java.util.ArrayList;
import java.util.HashMap;

import us.beamto.newplayer.common.VariablesList;
import us.beamto.newplayer.ui.activites.NewMediaPlayerActivity;
import us.beamto.newplayer.ui.adapters.ClickableListAdapter;

import android.content.Context;
import android.os.AsyncTask;

public class LoadAlbumPage extends
		AsyncTask<String, Void, ArrayList<HashMap<String, String>>> {
	String url = "";
	String pageNumber = "";
	ArrayList<HashMap<String, String>> list;
	String albumUrl = "";
	NewMediaPlayerActivity instance;

	@Override
	protected ArrayList<HashMap<String, String>> doInBackground(
			String... params) {
		NewMediaPlayerActivity instance = NewMediaPlayerActivity.getActivity();

		this.url = params[0];
		this.pageNumber = params[1];
		this.albumUrl = params[2];

		if (pageNumber.equals("1")) {
			list = new AlbumList(albumUrl).songList(url,
					VariablesList.JSON_PAGE_OBJECT, pageNumber);
		} else {
			list = new AlbumList(albumUrl).songList(url,
					VariablesList.JSON_PAGE_OBJECT, pageNumber);

		}
		if (list.size() == 0) {
			NewMediaPlayerActivity.setLastPage(true);
			instance.setLoading(false);

		} else {
			NewMediaPlayerActivity.setNewList(list);
			NewMediaPlayerActivity.appendToAdaptor();
			instance.setLoading(false);
		}
		return list;

	}

	@Override
	public void onPostExecute(ArrayList<HashMap<String, String>> result) {
		ClickableListAdapter adapter = NewMediaPlayerActivity.getClickableListAdapter();
		adapter.notifyDataSetChanged();
	}

}
