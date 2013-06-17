package com.example.newplayer;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class PlayListActivity extends Activity implements OnScrollListener {
	AssetManager am;
	ArrayList<HashMap<String, String>> songsList;
	int mVisibleThreashold = 6;
	boolean mLoading = false;
	boolean mLastPage = false;
	ClickableListAdapter adaptor;
	int numberOfPages = 1;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.grid_layout);
		String url = getResources().getString(R.string.albumsURL);
		am = this.getAssets();
		final Thread threadAlbums = new Thread() {
			public void run() {
				// String url = getResources().getString(R.string.albumsURL);
				// songsList = new AlbumList().songList(url);

				try {
					songsList = new AlbumList().sampleSongList(am
							.open("beamtoNew.json"));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				synchronized (this) {
					this.notifyAll();
				}
			}
		};
		synchronized (threadAlbums) {
			threadAlbums.start();
			try {
				threadAlbums.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		adaptor = new ClickableListAdapter(this, songsList);
		GridView view = (GridView) findViewById(R.id.grid_view);
		view.setAdapter(adaptor);
		view.setOnScrollListener(this);

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		int lastInScreen = firstVisibleItem + visibleItemCount;
		System.out.println(" %% FV " + firstVisibleItem);
		System.out.println(" %% LS " + lastInScreen);
		if (numberOfPages > 1) {
			mLastPage = true;
		}
		if (!mLastPage && !(mLoading) && (lastInScreen == totalItemCount)) {
			// new LoadAlbumList().execute("beamtoNew.json");
			numberOfPages++;
			AddToList(lastInScreen + 1);
		}
	}

	public void AddToList(int lastInScreen) {

		mLoading = true;
		ArrayList<HashMap<String, String>> newList = null;
		try {
			newList = new AlbumList().sampleSongList(am.open("beamtoNew.json"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (int i = 0; i < newList.size(); i++) {
			adaptor.addToList(newList.get(i));
		}
		adaptor.notifyDataSetChanged();
		mLoading = false;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub

	}

}