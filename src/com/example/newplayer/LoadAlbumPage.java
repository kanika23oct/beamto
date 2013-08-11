package com.example.newplayer;

import java.util.ArrayList;
import java.util.HashMap;

import android.os.AsyncTask;

public class LoadAlbumPage extends
AsyncTask<String, Void, ArrayList<HashMap<String, String>>> {
	String url = "";
	String pageNumber = "";
	ArrayList<HashMap<String, String>> list;
	String albumUrl = "";

	@Override
	protected ArrayList<HashMap<String, String>> doInBackground(
			String... params) {
		this.url = params[0];
		this.pageNumber = params[1];
		this.albumUrl = params[2];

		if (pageNumber.equals("1")) {
			list = new AlbumList(albumUrl).songList(url,
					VariablesList.JSON_PAGE_OBJECT, pageNumber);

			// NewMediaPlayer.setSongList(list);
		} else {
			list = new AlbumList(albumUrl).songList(url,
					VariablesList.JSON_PAGE_OBJECT, pageNumber);

		}
		NewMediaPlayer.setNewList(list);
		NewMediaPlayer.appendToAdaptor();

		if (list.size() == 0) {
			System.out.println("***** Albums Finish");
			NewMediaPlayer.setLastPage(true);
		}
        
		NewMediaPlayer.setLoading(false);
		return list;

	}

	@Override
	public void onPostExecute(ArrayList<HashMap<String, String>> result) {
		ClickableListAdapter adapter = NewMediaPlayer.getClickableListAdapter();
		adapter.notifyDataSetChanged();
		NewMediaPlayer.numberOfPages++;
		if (!NewMediaPlayer.mLastPage) {
			NewMediaPlayer.AddToList(NewMediaPlayer.numberOfPages);
		}
	}

}
